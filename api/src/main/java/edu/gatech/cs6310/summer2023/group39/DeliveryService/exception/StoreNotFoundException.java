package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class StoreNotFoundException extends DeliveryServiceException {
    public String name;
    public StoreNotFoundException(String name) {
        super(String.format("No store found with name %s", name));
        this.name = name;
    }
}
