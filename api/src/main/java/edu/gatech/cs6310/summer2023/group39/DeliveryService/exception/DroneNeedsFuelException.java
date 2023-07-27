package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Drone;

public class DroneNeedsFuelException extends DeliveryServiceException {
    public Drone drone;
    DroneNeedsFuelException(Drone drone) {
        super(String.format("Drone %s has no fuel", drone.getId()));
        this.drone = drone;
    }
}
