package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class CustomerExistsException extends DeliveryServiceException {
    public String account;
    public CustomerExistsException(String account) {
        super(String.format("A customer with the account %s already exists", account));
        this.account = account;
    }
}