package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import java.io.Serializable;

public class Efficiency implements Serializable {
    private int purchases;
    private int overloads;
    private int transfers;
    private GroceryStore store;
    public Efficiency(GroceryStore store) {
        this.store = store;
    }
    public int getPurchases() { return purchases; }
    public int getOverloads() { return overloads; }
    public int getTransfers() { return transfers; }
    public GroceryStore getStore() { return store; }
    public void incrementPurchases() { purchases += 1; }
    public void incrementOverloads(int n) { overloads += n; }
    public void incrementTransfers() { transfers += 1; }

}
