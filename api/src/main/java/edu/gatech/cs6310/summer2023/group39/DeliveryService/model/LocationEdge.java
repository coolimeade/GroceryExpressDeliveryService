package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

public class LocationEdge {
    private Location from;
    private Location to;
    public LocationEdge(Location from, Location to) {
        this.from = from;
        this.to = to;
    }
    public Location getFrom() { return from; }
    public Location getTo() { return to; }
    public long getDistance() { return from.distanceTo(to); }
}
