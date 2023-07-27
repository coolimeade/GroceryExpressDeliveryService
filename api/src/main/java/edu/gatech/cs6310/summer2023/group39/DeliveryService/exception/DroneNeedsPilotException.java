package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Drone;

public class DroneNeedsPilotException extends DeliveryServiceException {
    public Drone drone;
    public DroneNeedsPilotException(Drone drone) {
        super(String.format("Drone %s has no pilot", drone.getId()));
        this.drone = drone;
    }
}
