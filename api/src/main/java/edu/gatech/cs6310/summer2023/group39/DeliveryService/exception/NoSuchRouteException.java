package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Location;

public class NoSuchRouteException extends DeliveryServiceException {
    private Location from;
    private Location to;
    public NoSuchRouteException(Location from, Location to) {
        super("No route from " + from + " to " + to);
        this.from = from;
        this.to = to;
    }
    public Location getFrom() { return from; }
    public Location getTo() { return to; }
}
