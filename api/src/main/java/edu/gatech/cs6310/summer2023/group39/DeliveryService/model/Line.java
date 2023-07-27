package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import java.io.Serializable;

public class Line implements Serializable {
    private String storeName;
    private String itemName;
    private int unitWeight;
    private int quantity;
    private int unitPrice;

    public Line(String storeName, String itemName, int weight, int quantity, int unitPrice) {
        this.storeName = storeName;
        this.itemName = itemName;
        this.unitWeight = weight;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getUnitWeight() {
        return unitWeight;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getTotalPrice() {
        return quantity * unitPrice;
    }

    public int getWeight() {
        return quantity * unitWeight;
    }
}
