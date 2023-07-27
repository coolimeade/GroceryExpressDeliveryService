package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public  class OrderExistsException extends DeliveryServiceException {
    public String id;
    public OrderExistsException(String id) {
        super(String.format("An order with the id %s already exists", id));
        this.id = id;
    }
}