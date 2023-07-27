package edu.gatech.cs6310.summer2023.group39.DeliveryService.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Efficiency;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.GroceryStore;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StoreNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.EfficiencyService;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.GroceryStoreService;

@CrossOrigin(origins = "*")
@RestController
public class EfficiencyApiController {

    private EfficiencyService efficiencyService;
    private GroceryStoreService groceryStoreService;

    @Autowired
    public EfficiencyApiController(EfficiencyService efficiencyService, GroceryStoreService groceryStoreService) {
        this.efficiencyService = efficiencyService;
        this.groceryStoreService = groceryStoreService;
    }

    public static class EfficiencyInfo {
        private String storeName;
        private int purchases;
        private int overloads;
        private int transfers;
        public EfficiencyInfo(Efficiency efficiency) {
            this.storeName = efficiency.getStore().getName();
            this.purchases = efficiency.getPurchases();
            this.overloads = efficiency.getOverloads();
            this.transfers = efficiency.getTransfers();
        }
        public String getStoreName() { return storeName; }
        public int getPurchases() { return purchases; }
        public int getOverloads() { return overloads; }
        public int getTransfers() { return transfers; }
    }

    @GetMapping("/efficiencies")
    public ArrayList<EfficiencyInfo> getEfficiencies() {
        ArrayList<EfficiencyInfo> efficiencies = new ArrayList<EfficiencyInfo>();
        efficiencyService.forEachEfficiency((efficiency) -> {
            efficiencies.add(new EfficiencyInfo(efficiency));
        });
        return efficiencies;
    }

    @GetMapping("/efficiencies/{storeName}")
    public EfficiencyInfo getStoreEfficiency(@PathVariable String storeName)
            throws StoreNotFoundException {
        GroceryStore store = groceryStoreService.getStore(storeName);
        Efficiency e = efficiencyService.getEfficiency(store);
        return new EfficiencyInfo(e);
    }
}