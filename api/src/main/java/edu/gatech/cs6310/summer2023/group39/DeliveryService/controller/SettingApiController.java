package edu.gatech.cs6310.summer2023.group39.DeliveryService.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.DeliveryService;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Drone;

@CrossOrigin(origins = "*")
@RestController
public class SettingApiController {

    private DeliveryService deliveryService;

    @Autowired
    public SettingApiController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/settings/drones/fuelEfficiency")
    public int getFuelEfficiency() {
        return deliveryService.getFuelEfficiency();
    }

    @PutMapping("/settings/drones/fuelEfficiency")
    public void setFuelEfficiency(@RequestBody int fuelEfficiency) {
        deliveryService.setFuelEfficiency(fuelEfficiency);
    }

    @GetMapping("/settings/drones/refuellingRate")
    public int getRefuellingRate() {
        return Drone.getRefuellingRate();
    }

    @PutMapping("/settings/drones/refuellingRate")
    public void setRefuellingRate(@RequestBody int refuellingRate) {
        Drone.setRefuellingRate(refuellingRate);
    }
}