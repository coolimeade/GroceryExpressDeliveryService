package edu.gatech.cs6310.summer2023.group39.DeliveryService.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.PilotService;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.DeliveryService;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.PilotExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.PilotNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StoreNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.LicenseExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.DronePilot;

@CrossOrigin(origins = "*")
@RestController
public class PilotApiController {

    private PilotService pilotService;
    private DeliveryService deliveryService;

    @Autowired
    public PilotApiController(DeliveryService deliveryService, PilotService pilotService) {
        this.pilotService = pilotService;
        this.deliveryService = deliveryService;
    }

    public static class NewDronePilotRequest {
        private String account;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String taxId;
        private String license;
        private int experience;
        public String getAccount() { return account; }
        public String getLicense() { return license; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getTaxId() { return taxId; }
        public int getExperience() { return experience; }
    }

    @PostMapping("/pilots")
    public void makeDronePilot(@RequestBody NewDronePilotRequest request) throws PilotExistsException, LicenseExistsException {
        pilotService.makePilot(request.getAccount(), request.getFirstName(),
                request.getLastName(), request.getPhoneNumber(), request.getTaxId(),
                request.getLicense(), request.getExperience());
    }

    public static class PilotInfo {
        private String account;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String taxId;
        private String license;
        private int experience;
        private String droneId;
        public PilotInfo(DronePilot pilot) {
            this.account = pilot.getAccount();
            this.firstName = pilot.getFirstName();
            this.lastName = pilot.getLastName();
            this.phoneNumber = pilot.getPhoneNumber();
            this.taxId = pilot.getTaxId();
            this.license = pilot.getLicenseNumber();
            this.experience = pilot.getDeliveries();
            if (pilot.getDroneId() != null)
                this.droneId = pilot.getDroneId();
        }
        public String getAccount() { return account; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getTaxId() { return taxId; }
        public String getLicense() { return license; }
        public int getExperience() { return experience; }
        public String getDroneId() { return droneId; }
    }

    @GetMapping("/pilots")
    public ArrayList<PilotInfo> getAllDronePilots() {
        ArrayList<PilotInfo> pilots = new ArrayList<PilotInfo>();
        pilotService.forEachPilot((pilot) -> {
            pilots.add(new PilotInfo(pilot));
        });
        return pilots;
    }

    public static class FlyDroneRequest {
        private String storeName;
        private String droneId;
        public String getStoreName() { return storeName; }
        public String getDroneId() { return droneId; }
    }

    @PostMapping("/pilots/{pilotAccount}/fly")
    public void flyDrone(@PathVariable String pilotAccount, @RequestBody FlyDroneRequest request) throws PilotNotFoundException, DroneNotFoundException, StoreNotFoundException {
        deliveryService.flyDrone(request.getStoreName(), request.getDroneId(), pilotAccount);
    }
}