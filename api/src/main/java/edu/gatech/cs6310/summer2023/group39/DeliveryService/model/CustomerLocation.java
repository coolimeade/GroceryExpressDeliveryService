package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CustomerLocation extends Location {
    @JsonIgnore
    private Customer customer;

    public CustomerLocation(int lat, int lon, Customer customer) {
        super(lat, lon);
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean canRefuel() {
        return false;
    }
}
