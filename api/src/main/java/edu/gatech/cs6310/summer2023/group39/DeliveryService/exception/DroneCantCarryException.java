package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Drone;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.StoreItem;

public class DroneCantCarryException extends DeliveryServiceException {
    public Drone drone;
    public StoreItem item;
    public DroneCantCarryException(Drone drone, StoreItem item, int quantity) {
        super(String.format("Drone %s can't carry %d items of name %s (unit weight %d)", drone.getId(), quantity, item.getName(), item.getWeight( )));
        this.drone = drone;
        this.item = item;
    }
}