package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public  class PilotExistsException extends DeliveryServiceException {
    public String account;
    public PilotExistsException(String account) {
        super(String.format("A pilot with the account %s already exists", account));
        this.account = account;
    }
}
