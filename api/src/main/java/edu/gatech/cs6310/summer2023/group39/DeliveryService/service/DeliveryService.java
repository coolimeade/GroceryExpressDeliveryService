package edu.gatech.cs6310.summer2023.group39.DeliveryService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.*;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.*;

@Service
public class DeliveryService {

    private CustomerService customerService;
    private GroceryStoreService groceryStoreService;
    private DroneService droneService;
    private PilotService pilotService;

    @Autowired
    public DeliveryService(CustomerService customerService, GroceryStoreService groceryStoreService,
                           DroneService droneService,
                           PilotService pilotService) {
        this.customerService = customerService;
        this.groceryStoreService = groceryStoreService;
        this.droneService = droneService;
        this.pilotService = pilotService;
    }

    public void flyDrone(String storeName, String droneId, String pilotId)
        throws StoreNotFoundException, DroneNotFoundException, PilotNotFoundException
    {
        Drone drone = droneService.getDrone(storeName, droneId);
        DronePilot pilot = this.pilotService.getPilot(pilotId);
        String oldPilotId = drone.getPilotId();
        if (oldPilotId != null) {
            DronePilot oldPilot = pilotService.getPilot(oldPilotId);
            oldPilot.setDroneId(null);
            pilotService.setPilot(oldPilot);
        }
        drone.setPilotId(pilot.getAccount());
        droneService.setDrone(storeName, drone);
        String oldDroneId = pilot.getDroneId();
        if (oldDroneId != null) {
            Drone oldDrone = droneService.getDrone(storeName, oldDroneId);
            oldDrone.setPilotId(null);
            droneService.setDrone(oldDrone.getStoreName(), oldDrone);
        }
        pilot.setDroneId(drone.getId());
        pilotService.setPilot(pilot);
    }

    public long distanceToCustomer(String storeName, String account)
        throws StoreNotFoundException, CustomerNotFoundException
    {
        GroceryStore store = groceryStoreService.getStore(storeName);
        Customer customer = customerService.getCustomer(account);
        Location storeLocation = store.getLocation();
        Location customerLocation = customer.getLocation();
        return storeLocation.distanceTo(customerLocation);
    }

    public void setFuelEfficiency(int fuelEfficiency) {
        Drone.setFuelEfficiency(fuelEfficiency);
    }

    public int getFuelEfficiency() {
        return Drone.getFuelEfficiency();
    }
}