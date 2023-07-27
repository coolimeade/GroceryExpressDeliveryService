package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;
import java.io.Serializable;
import java.util.TreeMap;
import java.util.function.Consumer;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.*;

public class GroceryStore implements Serializable {

    private String name;
    private int revenue;
    private TreeMap<String,StoreItem> items = new TreeMap<String,StoreItem>();
    private Location location;

    public GroceryStore (String name, int revenue, int lat, int lon) {
        this.name = name;
        this.revenue = revenue;
        this.location = new GroceryStoreLocation(lat, lon, this);
    }

    public String getName() {
        return name;
    }

    public int getRevenue() {
        return revenue;
    }

    public Location getLocation() {
        return location;
    }

    public void sellItem(String itemName, int weight) throws ItemExistsException {
        if (items.containsKey(itemName))
            throw new ItemExistsException(itemName);
        items.put(itemName, new StoreItem(itemName, weight));
    }

    public void forEachItem(Consumer<StoreItem> consumer) {
        items.forEach((name, item) -> consumer.accept(item));
    }

    public StoreItem getItem(String name) throws ItemNotFoundException {
        if (!items.containsKey(name))
            throw new ItemNotFoundException(name);
        return items.get(name);
    }

    public void bookRevenue(int amount) {
        revenue += amount;
    }
}
