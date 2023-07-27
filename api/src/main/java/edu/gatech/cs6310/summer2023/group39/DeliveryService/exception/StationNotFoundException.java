package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class StationNotFoundException extends DeliveryServiceException {
    private String id;
    public StationNotFoundException(String id) {
        super(String.format("No station found with id %s", id));
        this.id = id;
    }
    public String getId() { return id; }
}