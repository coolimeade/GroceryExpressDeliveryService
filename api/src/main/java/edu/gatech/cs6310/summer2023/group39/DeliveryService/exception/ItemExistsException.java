package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class ItemExistsException extends DeliveryServiceException {
    public String name;
    public ItemExistsException(String name) {
        super(String.format("An item with the name %s already exists", name));
        this.name = name;
    }
}