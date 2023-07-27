package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class StoreExistsException extends DeliveryServiceException {
    public String name;
    public StoreExistsException(String name) {
        super(String.format("A store with the name %s already exists", name));
        this.name = name;
    }
}