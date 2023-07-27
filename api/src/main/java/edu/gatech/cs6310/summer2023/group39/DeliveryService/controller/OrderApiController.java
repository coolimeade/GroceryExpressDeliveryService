package edu.gatech.cs6310.summer2023.group39.DeliveryService.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.queue.MessagePublisher;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.queue.MessageSubscriber;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.OrderService;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Line;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Order;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StoreNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.CustomerNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.OrderExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.OrderNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.PilotNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.ItemNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.ItemAlreadyOrderedException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneCantCarryException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.CustomerCantAffordException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.InsufficientCapacityException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneNeedsFuelException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneNeedsPilotException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.NoSuchRouteException;

@CrossOrigin(origins = "*")
@RestController
public class OrderApiController {

    private static final Logger logger = LoggerFactory.getLogger(OrderApiController.class);

    private OrderService orderService;
    private MessagePublisher publisher;

    @Autowired
    public OrderApiController(OrderService orderService, MessagePublisher publisher, MessageSubscriber subscriber) {
        this.orderService = orderService;
        this.publisher = publisher;
    }

    public static class NewOrderRequest {
        private String account;
        private String orderId;
        private String droneId;
        public String getAccount() { return account; }
        public String getOrderId() { return orderId; }
        public String getDroneId() { return droneId; }
    }

    @PostMapping("/stores/{storeName}/orders")
    public void makeOrder(@PathVariable String storeName, @RequestBody NewOrderRequest request)
            throws StoreNotFoundException, CustomerNotFoundException, OrderExistsException, DroneNotFoundException
    {
        orderService.startOrder(storeName, request.getOrderId(),
                request.getDroneId(), request.getAccount());
    }

    public static class LineInfo {
        private String itemName;
        private int quantity;
        private int unitPrice;
        public LineInfo(Line line) {
            this.itemName = line.getItemName();
            this.quantity = line.getQuantity();
            this.unitPrice = line.getUnitPrice();
        }
        public String getItemName() { return itemName; }
        public int getQuantity() { return quantity; }
        public int getUnitPrice() { return unitPrice; }
    }

    public static class OrderInfo {
        private String orderId;
        private String droneId;
        private String customerAccount;
        private String storeName;
        public OrderInfo(Order order) {
            this.orderId = order.getId();
            this.droneId = order.getDroneId();
            this.customerAccount = order.getCustomerAccount();
            this.storeName = order.getStoreName();
        }
        public String getOrderId() { return orderId; }
        public String getDroneId() { return droneId; }
        public String getCustomerAccount() { return customerAccount; }
        public String getStoreName() { return storeName; }
    }

    @GetMapping("/stores/{storeName}/orders")
    public ArrayList<OrderInfo> getAllOrders(@PathVariable String storeName) throws StoreNotFoundException {
        ArrayList<OrderInfo> orders = new ArrayList<OrderInfo>();
        orderService.forEachOrder(storeName, (order) -> {
            orders.add(new OrderInfo(order));
        });
        return orders;
    }

    public static class NewLineRequest {
        private String itemName;
        private int quantity;
        private int unitPrice;
        public String getItemName() { return itemName; }
        public int getQuantity() { return quantity; }
        public int getUnitPrice() { return unitPrice; }
    }

    @PostMapping("/stores/{storeName}/orders/{orderId}/items")
    public void requestItem(@PathVariable String storeName, @PathVariable String orderId, @RequestBody NewLineRequest request)
            throws StoreNotFoundException, OrderNotFoundException, ItemNotFoundException, ItemAlreadyOrderedException, DroneCantCarryException, CustomerCantAffordException, DroneNotFoundException, CustomerNotFoundException
    {
        orderService.requestItem(
            storeName,
            orderId,
            request.getItemName(),
            request.getQuantity(),
            request.getUnitPrice()
        );
    }

    @GetMapping("/stores/{storeName}/orders/{orderId}/items")
    public ArrayList<LineInfo> getItems(@PathVariable String storeName, @PathVariable String orderId)
            throws StoreNotFoundException, OrderNotFoundException
    {
        ArrayList<LineInfo> lines = new ArrayList<LineInfo>();
        orderService.forEachLine(storeName, orderId, (line) -> {
            lines.add(new LineInfo(line));
        });
        return lines;
    }

    @PostMapping("/stores/{storeName}/orders/{orderId}/cancel")
    public void cancelOrder(@PathVariable String storeName, @PathVariable String orderId)
            throws StoreNotFoundException, OrderNotFoundException, DroneNotFoundException, CustomerNotFoundException
    {
        orderService.cancelOrder(storeName, orderId);
    }

    public static class TransferOrderRequest {
        private String droneId;
        public String getDroneId() { return droneId; }
    }

    @PostMapping("/stores/{storeName}/orders/{orderId}/transfer")
    public void transferOrder(@PathVariable String storeName, @PathVariable String orderId, @RequestBody TransferOrderRequest request)
            throws StoreNotFoundException,
            OrderNotFoundException,
            DroneNotFoundException,
            InsufficientCapacityException,
            PilotNotFoundException
    {
        orderService.transferOrder(storeName, orderId, request.getDroneId());
    }

    @PostMapping("/stores/{storeName}/orders/{orderId}/purchase")
    public void purchaseOrder(@PathVariable String storeName, @PathVariable String orderId)
            throws StoreNotFoundException,
            OrderNotFoundException,
            DroneNeedsFuelException,
            DroneNeedsPilotException,
            NoSuchRouteException,
            DroneNotFoundException,
            CustomerNotFoundException,
            PilotNotFoundException,
            InsufficientCapacityException
    {
        logger.info("purchaseOrder: " + storeName + " " + orderId);
        publisher.publish(storeName + ':' + orderId);
    }
}