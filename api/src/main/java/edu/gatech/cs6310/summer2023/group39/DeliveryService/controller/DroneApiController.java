package edu.gatech.cs6310.summer2023.group39.DeliveryService.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.DroneService;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StoreNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Drone;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Location;

@CrossOrigin(origins = "*")
@RestController
public class DroneApiController {

    private DroneService droneService;

    @Autowired
    public DroneApiController(DroneService droneService) {
        this.droneService = droneService;
    }

    public static class NewDroneRequest {
        private String droneId;
        private int capacity;
        private int fuel;
        private int maxFuel;
        private int speed;
        public String getDroneId() { return droneId; }
        public int getCapacity() { return capacity; }
        public int getFuel() { return fuel; }
        public int getMaxFuel() { return maxFuel; }
        public int getSpeed() { return speed; }
    }

    @PostMapping("/stores/{storeName}/drones")
    public void makeDrone(@PathVariable String storeName, @RequestBody NewDroneRequest request)
            throws StoreNotFoundException, DroneExistsException
    {
        droneService.makeDrone(storeName, request.getDroneId(), request.getCapacity(),
            request.getFuel(), request.getMaxFuel(), request.getSpeed());
    }

    public static class DroneInfo {
        private String droneId;
        private int capacity;
        private int fuel;
        private int maxFuel;
        private int speed;
        private String pilotAccount;
        private int lat;
        private int lon;
        private String state;
        private List<Location> route;
        private long distanceToNext;
        private int range;
        private int maxRange;
        private double bearingToNext;
        public DroneInfo(Drone drone) {
            this.droneId = drone.getId();
            this.capacity = drone.getCapacity();
            this.fuel = drone.getFuel();
            this.maxFuel = drone.getMaxFuel();
            this.speed = drone.getSpeed();
            if (drone.getPilotId() != null)
                this.pilotAccount = drone.getPilotId();
            this.lat = drone.getLocation().getLat();
            this.lon = drone.getLocation().getLon();
            this.state = drone.getState().toString();
            this.route = drone.getRoute();
            this.range = drone.getRange();
            this.maxRange = drone.getMaxRange();
            if (this.route != null && this.route.size() > 0) {
                this.distanceToNext = drone.getLocation().distanceTo(this.route.get(0));
                this.bearingToNext = Math.toDegrees(drone.getLocation().bearingTo(this.route.get(0)));
            }
        }
        public String getDroneId() { return droneId; }
        public int getCapacity() { return capacity; }
        public int getFuel() { return fuel; }
        public int getMaxFuel() { return maxFuel; }
        public int getSpeed() { return speed; }
        public String getPilotAccount() { return pilotAccount; }
        public int getLat() { return lat; }
        public int getLon() { return lon; }
        public String getState() { return state; }
        public List<Location> getRoute() { return route; }
        public long getDistanceToNext() { return distanceToNext; }
        public int getRange() { return range; }
        public int getMaxRange() { return maxRange; }
        public double getBearingToNext() { return bearingToNext; }
    }

    @GetMapping("/stores/{storeName}/drones")
    public List<DroneInfo> getAllDrones(@PathVariable String storeName) throws StoreNotFoundException {
        ArrayList<DroneInfo> drones = new ArrayList<DroneInfo>();
        droneService.forEachDrone(storeName, (drone) -> {
            drones.add(new DroneInfo(drone));
        });
        return drones;
    }
}