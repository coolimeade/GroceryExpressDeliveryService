package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import java.io.Serializable;

public class User implements Serializable {
    private String account;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    User(String account, String firstName, String lastName, String phoneNumber) {
        this.account = account;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
    public String getAccount() { return account; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
}
