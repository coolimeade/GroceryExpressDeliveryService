package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class StationExistsException extends DeliveryServiceException {
    private String id;
    public StationExistsException(String id) {
        super(String.format("A station with the id %s already exists", id));
        this.id = id;
    }
    public String getId() { return id; }
}