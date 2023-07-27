package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import java.io.Serializable;
import java.util.List;

public class Drone implements Serializable {
    private static int fuelEfficiency = 1;
    private static int refuellingRate = 10;

    public static enum State {
        AcceptingOrders,
        RefuellingAtHome,
        FlyingOut,
        RefuellingOut,
        CompletingDelivery,
        Stranded,
        FlyingBack,
        RefuellingBack
    };

    private State state;
    private String id;
    private int fuel;
    private int maxFuel;
    private int capacity;
    private int speed;
    private Location location;
    private Location home;
    private List<Location> route;
    private String pilotId = null;
    private String storeName;

    public Drone(String storeName, String id, int capacity, int fuel, int maxFuel, int speed, Location location, State state) {
        if (fuel < 0 || fuel > maxFuel)
            throw new IllegalArgumentException("Fuel must be between 0 and maxFuel");
        this.state = state;
        this.id = id;
        this.fuel = fuel;
        this.maxFuel = maxFuel;
        this.capacity = capacity;
        this.speed = speed;
        this.location = location;
        this.home = location;
        this.storeName = storeName;
    }

    public String getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFuel() {
        return fuel;
    }

    public int getMaxFuel() {
        return maxFuel;
    }

    public int getSpeed() {
        return speed;
    }

    public Location getLocation() {
        return location;
    }

    public String getPilotId() {
        return pilotId;
    }

    public void setPilotId(String pilotId) {
        this.pilotId = pilotId;
    }

    public static int getFuelEfficiency() {
        return fuelEfficiency;
    }

    public static void setFuelEfficiency(int fuelEfficiency) {
        Drone.fuelEfficiency = fuelEfficiency;
    }

    public static int getRefuellingRate() {
        return refuellingRate;
    }

    public static void setRefuellingRate(int refuellingRate) {
        Drone.refuellingRate = refuellingRate;
    }

    public int getRange() {
        return fuel * fuelEfficiency;
    }

    public int getMaxRange() {
        return maxFuel * fuelEfficiency;
    }

    public Location getHome() {
        return home;
    }

    public State getState() {
        return state;
    }

    public String getStoreName() {
        return storeName;
    }

    public List<Location> getRoute() {
        return route;
    }

    public void setRoute(List<Location> route) {
        this.route = route;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setState(State state) {
        this.state = state;
    }
}
