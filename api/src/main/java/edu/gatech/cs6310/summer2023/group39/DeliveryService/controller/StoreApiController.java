package edu.gatech.cs6310.summer2023.group39.DeliveryService.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.GroceryStoreService;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.DeliveryService;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StoreNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StoreExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.CustomerNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.ItemExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.GroceryStore;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.StoreItem;

@CrossOrigin(origins = "*")
@RestController
public class StoreApiController {

    private GroceryStoreService groceryStoreService;
    private DeliveryService deliveryService;

    @Autowired
    public StoreApiController(GroceryStoreService groceryStoreService, DeliveryService deliveryService) {
        this.groceryStoreService = groceryStoreService;
        this.deliveryService = deliveryService;
    }

    public static class NewStoreRequest {
        private String name;
        private int revenue;
        private int lat;
        private int lon;
        public String getName() { return name; }
        public int getRevenue() { return revenue; }
        public int getLat() { return lat; }
        public int getLon() { return lon; }
    }

    @PostMapping("/stores")
    public Store makeStore(@RequestBody NewStoreRequest request) throws StoreExistsException{
        groceryStoreService.makeStore(request.getName(), request.getRevenue(), request.getLat(), request.getLon());
        return new Store(request.getName(), request.getRevenue());
    }

    public static class Store {
        private String name;
        private int revenue;
        public Store(String name, int revenue) {
            this.name = name;
            this.revenue = revenue;
        }
        public String getName() { return name; }
        public int getRevenue() { return revenue; }
    }

    @GetMapping("/stores")
    public ArrayList<GroceryStore> getAllStores() {
        ArrayList<GroceryStore> stores = new ArrayList<GroceryStore>();
        groceryStoreService.forEachStore((groceryStore) -> {
            stores.add(groceryStore);
        });
        return stores;
    }

    public static class NewItemRequest {
        private String itemName;
        private int weight;
        public String getItemName() { return itemName; }
        public int getWeight() { return weight; }
    }


    @GetMapping("/stores/{storeName}")
    public GroceryStore getStore(@PathVariable String storeName) throws StoreNotFoundException {
        return groceryStoreService.getStore(storeName);
    }

    @PostMapping("/stores/{storeName}/items")
    public void sellItem(@PathVariable String storeName, @RequestBody NewItemRequest request) throws StoreNotFoundException, ItemExistsException {
        groceryStoreService.sellItem(storeName, request.getItemName(), request.getWeight());
    }

    @GetMapping("/stores/{storeName}/items")
    public ArrayList<StoreItem> getItems(@PathVariable String storeName) throws StoreNotFoundException {
        ArrayList<StoreItem> items = new ArrayList<StoreItem>();
        groceryStoreService.forEachItem(storeName, (item) -> {
            items.add(item);
        });
        return items;
    }

    @GetMapping("/stores/{storeName}/distance/{accountId}")
    public long storeDistance(@PathVariable String storeName, @PathVariable String accountId)
            throws StoreNotFoundException,
            CustomerNotFoundException
    {
        return deliveryService.distanceToCustomer(storeName, accountId);
    }
}