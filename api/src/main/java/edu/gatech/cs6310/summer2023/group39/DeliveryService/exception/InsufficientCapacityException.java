package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Drone;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Order;

public class InsufficientCapacityException extends DeliveryServiceException {
    Drone drone;
    Order order;
    public InsufficientCapacityException(Drone drone, Order order) {
        super(String.format("Drone %s cannot carry order %s", drone.getId(), order.getId()));
        this.drone = drone;
        this.order = order;
    }

}
