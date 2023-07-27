package edu.gatech.cs6310.summer2023.group39.DeliveryService.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.NoSuchRouteException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StoreNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Drone;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.GroceryStore;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.LocationEdge;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Location;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Customer;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Order;

@Service
public class DroneService {

    private static final Logger logger = LoggerFactory.getLogger(DroneService.class);

    private final String TYPE = "DRONE";
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Drone> hashOperations;

    private GroceryStoreService groceryStoreService;
    private StationService stationService;

    private Timer timer;

    @Autowired
    public DroneService(RedisTemplate<String, Object> redisTemplate,
        GroceryStoreService groceryStoreService,
        StationService stationService) {
        this.groceryStoreService = groceryStoreService;
        this.stationService = stationService;
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                forEachDrone((Drone drone) -> {
                    updateDroneState(drone);
                });
            }
        }, 0, 1000);
    }

    public Drone makeDrone(String storeName, String droneId, int capacity, int fuel, int maxFuel, int speed)
            throws StoreNotFoundException, DroneExistsException {
        GroceryStore store = groceryStoreService.getStore(storeName);
        Drone.State state = (fuel < maxFuel) ? Drone.State.RefuellingAtHome : Drone.State.AcceptingOrders;
        logger.info("Making drone " + droneId + " for store " + storeName);
        Drone drone = new Drone(storeName, droneId, capacity, fuel, maxFuel, speed, store.getLocation(), state);
        saveDrone(drone);
        try {
            Drone other = getDrone(storeName, droneId);
            logger.info("Drone " + other.getId() + " successfully created for store " + other.getStoreName());
        } catch (DroneNotFoundException e) {
            logger.error("Drone " + droneId + " not found after creation");
        }
        return drone;
    }

    public Drone getDrone(String storeName, String droneId) throws StoreNotFoundException, DroneNotFoundException {
        if (!groceryStoreService.hasStore(storeName)) {
            throw new StoreNotFoundException(storeName);
        }
        if (!this.hashOperations.hasKey(TYPE + ":" + storeName, droneId))
            throw new DroneNotFoundException(droneId);
        return this.hashOperations.get(TYPE + ":" + storeName, droneId);
    }

    public void forEachDrone(String storeName, Consumer<Drone> consumer)
        throws StoreNotFoundException
    {
        if (!groceryStoreService.hasStore(storeName)) {
            throw new StoreNotFoundException(storeName);
        }
        this.hashOperations.values(TYPE + ":" + storeName).forEach(consumer);
    }

    public void setDrone(String storeName, Drone drone) {
        hashOperations.put(TYPE + ":" + storeName, drone.getId(), drone);
        return;
    }

    public void deliverOrder(Drone drone, Order order, Customer customer)
        throws NoSuchRouteException
    {
        logger.info("Drone " + drone.getId() + " delivering order " + order.getId() + " to customer " + customer.getAccount());
        List<Location> route = calculateRoute(drone.getLocation(), customer.getLocation(), drone.getRange(), drone.getMaxRange());
        logger.info("Drone " + drone.getId() + " route: " + route);
        drone.setRoute(route);
        drone.setState(Drone.State.FlyingOut);
        logger.info("Drone " + drone.getId() + " saving");
        saveDrone(drone);
        logger.info("Delivery started");
    }

    private void updateDroneState(Drone drone) {
        Drone.State state = drone.getState();
        List<Location> route = null;
        switch (state) {
            case RefuellingAtHome:
                drone.setFuel(drone.getFuel() +
                    Math.min(Drone.getRefuellingRate(), drone.getMaxFuel() - drone.getFuel()));
                if (drone.getFuel() >= drone.getMaxFuel()) {
                    drone.setState(Drone.State.AcceptingOrders);
                }
                saveDrone(drone);
                break;
            case RefuellingOut:
                drone.setFuel(drone.getFuel() +
                    Math.min(Drone.getRefuellingRate(), drone.getMaxFuel() - drone.getFuel()));
                if (drone.getFuel() >= drone.getMaxFuel()) {
                   drone.setState(Drone.State.FlyingOut);
                }
                saveDrone(drone);
                break;
            case RefuellingBack:
                drone.setFuel(drone.getFuel() +
                    Math.min(Drone.getRefuellingRate(), drone.getMaxFuel() - drone.getFuel()));
                if (drone.getFuel() >= drone.getMaxFuel()) {
                   drone.setState(Drone.State.FlyingBack);
                }
                saveDrone(drone);
                break;
            case FlyingOut:
                route = drone.getRoute();
                flyToward(drone, route.get(0), 1);
                if (drone.getLocation().equals(route.get(0))) {
                    route.remove(0);
                    drone.setRoute(route);
                    if (route.size() == 0) {
                        drone.setState(Drone.State.CompletingDelivery);
                        try {
                            route = calculateRoute(drone.getLocation(), drone.getHome(), drone.getRange(), drone.getMaxRange());
                            drone.setRoute(route);
                            drone.setState(Drone.State.FlyingBack);
                        } catch (NoSuchRouteException e) {
                            drone.setState(Drone.State.Stranded);
                        }
                    } else {
                        drone.setState(Drone.State.RefuellingOut);
                    }
                }
                saveDrone(drone);
                break;
            case FlyingBack:
                route = drone.getRoute();
                flyToward(drone, route.get(0), 1);
                if (drone.getLocation().equals(route.get(0))) {
                    route.remove(0);
                    drone.setRoute(route);
                    if (route.size() == 0) {
                        drone.setState(Drone.State.RefuellingAtHome);
                    } else {
                        drone.setState(Drone.State.RefuellingBack);
                    }
                }
                saveDrone(drone);
                break;
            case AcceptingOrders:
            case CompletingDelivery:
            case Stranded:
                break;
        }
    }

    public class RefuellingNetwork {
        private GroceryStoreService groceryStoreService;
        private StationService stationService;

        public RefuellingNetwork(GroceryStoreService groceryStoreService, StationService stationService) {
            this.groceryStoreService = groceryStoreService;
            this.stationService = stationService;
        }
        public ArrayList<LocationEdge> edgesFrom(Location location, int maxDistance) {
            ArrayList<LocationEdge> edges = new ArrayList<LocationEdge>();
            groceryStoreService.forEachStore(store -> {
                if (!location.equals(store.getLocation()) && location.distanceTo(store.getLocation()) <= maxDistance) {
                    edges.add(new LocationEdge(location, store.getLocation()));
                }
            });
            stationService.forEachRefuellingStation(refuellingStation -> {
                if (!location.equals(refuellingStation.getLocation()) && location.distanceTo(refuellingStation.getLocation()) <= maxDistance) {
                    edges.add(new LocationEdge(location, refuellingStation.getLocation()));
                }
            });
            return edges;
        }
    }

    private ArrayList<Location> calculateRoute(Location start, Location end, int range, int maxRange)
        throws NoSuchRouteException
    {
        RefuellingNetwork network = new RefuellingNetwork(this.groceryStoreService, this.stationService);
        var distances = new HashMap<Location,Long>();
        var queue = new PriorityQueue<Location>(
            Comparator.comparing(node -> distances.getOrDefault(node, Long.MAX_VALUE))
        );
        var previous = new HashMap<Location,Location>();
        var first = true;
        // Add the start node to the queue with a distance of 0
        queue.add(start);
        distances.put(start, 0L);

        while (!queue.isEmpty()) {
            // Get the node with the smallest distance
            var node = queue.poll();

            logger.info("Visiting node " + node);
            // If this node is the end node, we're done
            if (node.equals(end)) {
                break;
            }

            if (node.distanceTo(end) <= ((first) ? range : maxRange)) {
                var neighbor = end;

                logger.info("Endpoint is within range: " + node.distanceTo(end) + " <= " + ((first) ? range : maxRange) );

                var newDistance = distances.get(node) + node.distanceTo(end);

                // If this is a shorter path to the neighbor, update the distances and previous nodes
                if (newDistance < distances.getOrDefault(neighbor, Long.MAX_VALUE)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, node);
                    queue.add(neighbor);
                }
            } else {
                logger.info("Endpoint is not within range: " + node.distanceTo(end) + " > " + ((first) ? range : maxRange) );
            }
            // Visit each neighbor of the current node
            for (LocationEdge edge : network.edgesFrom(node, (first) ? range : maxRange )) {
                var neighbor = edge.getTo();

                logger.info("Neighbour is within range: " + node.distanceTo(neighbor) + " <= " + ((first) ? range : maxRange) );

                var newDistance = distances.get(node) + edge.getDistance();

                // If this is a shorter path to the neighbor, update the distances and previous nodes
                if (newDistance < distances.getOrDefault(neighbor, Long.MAX_VALUE)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, node);
                    queue.add(neighbor);
                }
            }
            first = false;
        }

        if (previous.get(end) == null) {
            throw new NoSuchRouteException(start, end);
        }

        ArrayList<Location> route = new ArrayList<Location>();

        for (Location node = end; node != start; node = previous.get(node)) {
            route.add(node);
        }

        Collections.reverse(route);
        return route;
    }

    private void flyToward(Drone drone, Location goal, int time) {
        int speed = drone.getSpeed();
        Location location = drone.getLocation();
        int fuel = drone.getFuel();
        Location oldLocation = location;
        int oldFuel = fuel;
        int fuelEfficiency = Drone.getFuelEfficiency();
        double R = 6371e3; // Earth's radius in meters
        double dist = speed * time; // distance = speed * time

        logger.info("Flying drone " + drone.getId() + " from " + location + " to " + goal + " at speed " + speed + " for " + time + " seconds" + " (" + dist + " meters)");

        if (dist >= location.distanceTo(goal)) {
            logger.info("Drone " + drone.getId() + " arrived at " + goal);
            location = goal;
            fuel -= Math.round((1.0 * location.distanceTo(goal)) / (1.0 * fuelEfficiency));
        } else {
            logger.info("Drone " + drone.getId() + " still flying");

            double lat1 = Math.toRadians(location.getLat() / 100000.0);
            double lon1 = Math.toRadians(location.getLon() / 100000.0);
            double lat2 = Math.toRadians(goal.getLat() / 100000.0);
            double lon2 = Math.toRadians(goal.getLon() / 100000.0);

            // Calculate the bearing
            double dLon = lon2 - lon1;
            double y = Math.sin(dLon) * Math.cos(lat2);
            double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
            double brng = Math.atan2(y, x);

            // Calculate the new point
            double lat3 = Math.asin(Math.sin(lat1) * Math.cos(dist / R) + Math.cos(lat1) * Math.sin(dist / R) * Math.cos(brng));
            double lon3 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(dist / R) * Math.cos(lat1), Math.cos(dist / R) - Math.sin(lat1) * Math.sin(lat3));

            // Convert back to degrees
            lat3 = Math.toDegrees(lat3);
            lon3 = Math.toDegrees(lon3);

            // Update the location
            location = new Location((int)(lat3 * 100000.0), (int)(lon3 * 100000.0));
            fuel -= Math.round((1.0 * dist) / (1.0 * fuelEfficiency));
        }

        logger.info("Drone " + drone.getId() + " flew from " + oldLocation + " to " + location + " at speed " + speed + " for " + time + " seconds" + " (" + dist + " meters) using " + (oldFuel - fuel) + " fuel");

        drone.setFuel(fuel);
        drone.setLocation(location);
    }

    private void saveDrone(Drone drone) {
        this.hashOperations.put(TYPE + ":" + drone.getStoreName(), drone.getId(), drone);
    }

    private void forEachDrone(Consumer<Drone> consumer) {
        groceryStoreService.forEachStore(store -> {
            this.hashOperations.values(TYPE + ":" + store.getName()).forEach(consumer);
        });
    }
}
