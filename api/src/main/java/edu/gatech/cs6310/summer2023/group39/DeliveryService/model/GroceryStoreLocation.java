package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GroceryStoreLocation extends Location {

    @JsonIgnore
    private GroceryStore groceryStore;

    public GroceryStoreLocation(int lat, int lon, GroceryStore groceryStore) {
        super(lat, lon);
        this.groceryStore = groceryStore;
    }

    public boolean canRefuel() {
        return true;
    }

    public GroceryStore getGroceryStore() {
        return groceryStore;
    }
}
