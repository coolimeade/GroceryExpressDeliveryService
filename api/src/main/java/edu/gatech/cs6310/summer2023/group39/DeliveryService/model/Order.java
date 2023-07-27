package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import java.io.Serializable;

public class Order implements Serializable {

    private String droneId;
    private String storeName;
    private String customerAccount;
    private String id;

    public Order(String id, GroceryStore store, Customer customer, Drone drone) {
        this.id = id;
        this.droneId = drone.getId();
        this.storeName = store.getName();
        this.customerAccount = customer.getAccount();
    }

    public String getDroneId() {
        return droneId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getCustomerAccount() {
        return customerAccount;
    }

    public String getId() {
        return id;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }
}
