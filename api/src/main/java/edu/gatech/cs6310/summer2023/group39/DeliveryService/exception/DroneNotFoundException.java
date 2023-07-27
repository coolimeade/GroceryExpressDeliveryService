package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class DroneNotFoundException extends DeliveryServiceException {
    public String id;
    public DroneNotFoundException(String id) {
        super(String.format("A drone with the id %s was not found", id));
        this.id = id;
    }
}