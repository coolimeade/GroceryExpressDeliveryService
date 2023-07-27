package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class CustomerNotFoundException extends DeliveryServiceException {
    public String account;
    public CustomerNotFoundException(String account) {
        super(String.format("No customer found with account %s", account));
        this.account = account;
    }
}