package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import java.io.Serializable;

public class RefuellingStation implements Serializable {
    private String id;
    private Location location;

    public RefuellingStation(String id, int lat, int lon) {
        this.id = id;
        this.location = new RefuellingStationLocation(lat, lon, this);
    }

    public String getId() { return id; }
    public Location getLocation() { return location; }
    public void setLocation(int lat, int lon) {
        this.location = new RefuellingStationLocation(lat, lon, this);
    }
}