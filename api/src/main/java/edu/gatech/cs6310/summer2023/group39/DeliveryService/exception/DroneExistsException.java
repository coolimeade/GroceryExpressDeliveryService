package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class DroneExistsException extends DeliveryServiceException {
    public String id;
    public DroneExistsException(String id) {
        super(String.format("A drone with the id %s already exists", id));
        this.id = id;
    }
}