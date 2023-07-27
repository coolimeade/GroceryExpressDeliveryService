package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Order;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.StoreItem;

public class ItemAlreadyOrderedException extends DeliveryServiceException {
    public Order order;
    public StoreItem item;
    public ItemAlreadyOrderedException(Order order, StoreItem item) {
        super(String.format("An item with the name %s already exists in order %s", item.getName(), order.getId( )));
        this.order = order;
        this.item = item;
    }
}