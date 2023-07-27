package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Customer;

public class CustomerCantAffordException extends DeliveryServiceException {
    public Customer customer;
    public int quantity;
    public int unitPrice;
    public CustomerCantAffordException(Customer customer, int quantity, int unitPrice) {
        super(String.format("Customer %s can't afford %d items with unit price %d", customer.getAccount(), quantity, unitPrice));
        this.customer = customer;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}