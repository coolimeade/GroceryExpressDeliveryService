package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import java.io.Serializable;

public class StoreItem implements Serializable {
    private String name;
    private int weight;

    StoreItem (String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }
}
