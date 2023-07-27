package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class OrderNotFoundException extends DeliveryServiceException {
    public String id;
    public OrderNotFoundException(String id) {
        super(String.format("An with the id %s was not found", id));
        this.id = id;
    }
}