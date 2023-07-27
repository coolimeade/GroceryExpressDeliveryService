package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public class PilotNotFoundException extends DeliveryServiceException {
    public String account;
    public PilotNotFoundException(String account) {
        super(String.format("No pilot found with account number %s", account));
        this.account = account;
    }
}
