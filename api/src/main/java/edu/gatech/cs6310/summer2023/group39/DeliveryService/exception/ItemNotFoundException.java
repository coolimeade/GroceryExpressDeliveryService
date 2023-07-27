package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public  class ItemNotFoundException extends DeliveryServiceException {
    public String name;
    public ItemNotFoundException(String name) {
        super(String.format("An item with the name %s was not found", name));
        this.name = name;
    }
}