package edu.gatech.cs6310.summer2023.group39.DeliveryService.service;

import java.util.function.Consumer;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.CustomerCantAffordException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.CustomerNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneCantCarryException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneNeedsFuelException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneNeedsPilotException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DroneNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.InsufficientCapacityException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.ItemAlreadyOrderedException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.ItemNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.NoSuchRouteException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.OrderExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.OrderNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.PilotNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StoreNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Customer;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Drone;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.DronePilot;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.GroceryStore;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Order;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.StoreItem;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Line;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Efficiency;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final String TYPE = "ORDER";
    private final String DRONEORDER = "DRONEORDER";
    private final String CUSTOMERORDER = "CUSTOMERORDER";
    private final String LINE = "LINE";

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;
    private final ListOperations<String, Object> listOperations;

    private GroceryStoreService groceryStoreService;
    private CustomerService customerService;
    private DroneService droneService;
    private PilotService pilotService;
    private EfficiencyService efficiencyService;

    @Autowired
    public OrderService(RedisTemplate<String, Object> redisTemplate, GroceryStoreService groceryStoreService, CustomerService customerService, DroneService droneService, PilotService pilotService, EfficiencyService efficiencyService) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
        this.listOperations = this.redisTemplate.opsForList();
        this.groceryStoreService = groceryStoreService;
        this.customerService = customerService;
        this.droneService = droneService;
        this.pilotService = pilotService;
        this.efficiencyService = efficiencyService;
    }

    public String orderRef(Order order) {
        return order.getStoreName() + ":" + order.getId();
    }

    public void startOrder(String storeName, String orderId, String droneId, String account)
        throws StoreNotFoundException, OrderExistsException, DroneNotFoundException, CustomerNotFoundException
    {
        GroceryStore store = groceryStoreService.getStore(storeName);
        Customer customer = customerService.getCustomer(account);
        Drone drone = droneService.getDrone(storeName, droneId);
        Order order = new Order(orderId, store, customer, drone);
        hashOperations.put(TYPE + ":" + storeName, orderId, order);
        String ref = orderRef(order);
        listOperations.leftPush(DRONEORDER + ":" + storeName + ":" + droneId, ref);
        listOperations.leftPush(CUSTOMERORDER + ":" + customer.getAccount(), ref);
    }

    public void forEachOrder(String storeName, Consumer<Order> consumer)
        throws StoreNotFoundException
    {
        if (!groceryStoreService.hasStore(storeName)) {
            throw new StoreNotFoundException(storeName);
        }
        hashOperations.values(TYPE + ":" + storeName).forEach((Object obj) -> consumer.accept((Order) obj));
    }

    public Order getOrder(String storeName, String orderId)
        throws StoreNotFoundException, OrderNotFoundException
    {
        if (!groceryStoreService.hasStore(storeName)) {
            throw new StoreNotFoundException(storeName);
        }
        if (!hashOperations.hasKey(TYPE + ":" + storeName, orderId)) {
            throw new OrderNotFoundException(orderId);
        }
        return (Order) hashOperations.get(TYPE + ":" + storeName, orderId);
    }

    public void requestItem(String storeName, String orderId, String itemName, int quantity, int unitPrice)
        throws StoreNotFoundException, OrderNotFoundException, ItemNotFoundException, ItemAlreadyOrderedException, DroneCantCarryException, CustomerCantAffordException, DroneNotFoundException, CustomerNotFoundException
    {
        GroceryStore store = groceryStoreService.getStore(storeName);
        Order order = getOrder(storeName, orderId);
        StoreItem item = store.getItem(itemName);
        Drone drone = droneService.getDrone(storeName, order.getDroneId());
        Customer customer = customerService.getCustomer(order.getCustomerAccount());
        if (hashOperations.hasKey(LINE + ":" + storeName + ":" + orderId, itemName)) {
            throw new ItemAlreadyOrderedException(order, item);
        }
        if (!canAfford(customer, quantity, unitPrice)) {
            throw new CustomerCantAffordException(customer, quantity, unitPrice);
        }
        if (!canCarry(storeName, drone, quantity, item.getWeight())) {
            throw new DroneCantCarryException(drone, item, quantity);
        }
        hashOperations.put(LINE + ":" + storeName + ":" + orderId, item.getName(), new Line(storeName, itemName, item.getWeight(), quantity, unitPrice));
    }

    public boolean hasOrder(String storeName, String orderId) {
        return hashOperations.hasKey(TYPE + ":" + storeName, orderId);
    }

    public void purchaseOrder(String storeName, String orderId)
        throws StoreNotFoundException, OrderNotFoundException, DroneNotFoundException, DroneNeedsPilotException, DroneNeedsFuelException, NoSuchRouteException, PilotNotFoundException, CustomerNotFoundException, InsufficientCapacityException
    {
        GroceryStore store = groceryStoreService.getStore(storeName);
        Order order = getOrder(storeName, orderId);
        String droneId = order.getDroneId();
        Drone drone = droneService.getDrone(storeName, droneId);
        DronePilot pilot = pilotService.getPilot(drone.getPilotId());
        if (pilot == null) {
            throw new DroneNeedsPilotException(drone);
        }
        logger.info("Pilot {} is flying drone {} to deliver order {} to customer {}",
            pilot.getAccount(), drone.getId(), order.getId(), order.getCustomerAccount());
        Customer customer = customerService.getCustomer(order.getCustomerAccount());
        droneService.deliverOrder(drone, order, customer);
        int totalPrice = getOrderPrice(storeName, orderId);
        customerService.deductCredit(order.getCustomerAccount(), totalPrice);
        groceryStoreService.bookRevenue(storeName, totalPrice);
        String ref = orderRef(order);
        listOperations.remove(DRONEORDER + ":" + storeName + ":" + droneId, 1, ref);
        listOperations.remove(CUSTOMERORDER + ":" + order.getCustomerAccount(), 1, ref);
        hashOperations.delete(TYPE + ":" + storeName, orderId);
        Efficiency efficiency = efficiencyService.getEfficiency(store);
        if (efficiency == null) {
            efficiency = new Efficiency(store);
        }
        efficiency.incrementPurchases();
        efficiency.incrementOverloads(getDroneOrderSize(storeName, drone.getId()));
        efficiencyService.saveEfficiency(storeName, efficiency);
    }

    public void cancelOrder(String storeName, String orderId)
        throws StoreNotFoundException, OrderNotFoundException, DroneNotFoundException, CustomerNotFoundException
    {
        if (!groceryStoreService.hasStore(storeName)) {
            throw new StoreNotFoundException(storeName);
        }
        Order order = getOrder(storeName, orderId);
        String droneId = order.getDroneId();
        Customer customer = customerService.getCustomer(order.getCustomerAccount());
        String ref = orderRef(order);
        listOperations.remove(DRONEORDER + ":" + storeName + ":" + droneId, 1, ref);
        listOperations.remove(CUSTOMERORDER + ":" + customer.getAccount(), 1, ref);
        hashOperations.delete(TYPE + ":" + storeName, orderId);
    }

    public boolean transferOrder(String storeName, String orderId, String droneId)
        throws StoreNotFoundException, OrderNotFoundException, DroneNotFoundException, InsufficientCapacityException
    {
        GroceryStore store = groceryStoreService.getStore(storeName);
        Order order = getOrder(storeName, orderId);
        Drone newDrone = droneService.getDrone(storeName, droneId);
        Drone oldDrone = droneService.getDrone(storeName, order.getDroneId());
        if (newDrone.getId() == oldDrone.getId())
            return false;
        if (!canCarry(storeName, newDrone, 1, getOrderWeight(storeName, orderId))) {
            throw new InsufficientCapacityException(newDrone, order);
        }
        String ref = orderRef(order);
        listOperations.remove(DRONEORDER + ":" + storeName + ":" + oldDrone.getId(), 1, ref);
        listOperations.leftPush(DRONEORDER + ":" + storeName + ":" + newDrone.getId(), ref);
        hashOperations.put(TYPE + ":" + storeName, orderId, order);
        Efficiency efficiency = efficiencyService.getEfficiency(store);
        if (efficiency == null) {
            efficiency = new Efficiency(store);
        }
        efficiency.incrementTransfers();
        efficiencyService.saveEfficiency(storeName, efficiency);
        return true;
    }

    public void forEachLine(String storeName, String orderId, Consumer<Line> consumer) {
        hashOperations.values(LINE + ":" + storeName + ":" + orderId).forEach((Object obj) ->
            consumer.accept((Line) obj)
        );
    }

    public boolean canCarry(String storeName, Drone drone, int quantity, int weight)
        throws StoreNotFoundException, DroneNotFoundException, OrderNotFoundException
    {
        int totalWeight = 0;
        List<Order> orders = getDroneOrders(storeName, drone.getId());
        for (Order order: orders) {
            totalWeight += getOrderWeight(order.getStoreName(), order.getId());
        }
        return totalWeight + (weight * quantity) <= drone.getCapacity();
    }

    public List<Order> getDroneOrders(String storeName, String droneId)
        throws StoreNotFoundException, DroneNotFoundException, OrderNotFoundException
    {
        ArrayList<Order> orders = new ArrayList<Order>();
        List<Object> refs = listOperations.range(DRONEORDER + ":" + storeName + ":" + droneId, 0, -1);
        if (refs == null) {
            return orders;
        }
        for (Object ref : refs) {
            String orderRef = (String) ref;
            String[] parts = orderRef.split(":");
            orders.add(getOrder(parts[0], parts[1]));
        }
        return orders;
    }

    public int getOrderWeight(String storeName, String orderId) {
        int weight = 0;
        List<Object> lines = hashOperations.values(LINE + ":" + storeName + ":" + orderId);
        for (Object obj : lines) {
            Line line = (Line) obj;
            weight += line.getWeight();
        }
        return weight;
    }

    public int getOrderPrice(String storeName, String orderId) {
        int price = 0;
        List<Object> lines = hashOperations.values(LINE + ":" + storeName + ":" + orderId);
        for (Object obj : lines) {
            Line line = (Line) obj;
            price += line.getTotalPrice();
        }
        return price;
    }

    public List<Order> getCustomerOrders(String account)
        throws CustomerNotFoundException, OrderNotFoundException, StoreNotFoundException
    {
        ArrayList<Order> orders = new ArrayList<Order>();
        List<Object> refs = listOperations.range(CUSTOMERORDER + ":" + account, 0, -1);
        if (refs == null) {
            return orders;
        }
        for (Object ref : refs) {
            String orderRef = (String) ref;
            String[] parts = orderRef.split(":");
            orders.add(getOrder(parts[0], parts[1]));
        }
        return orders;
    }

    public boolean canAfford(Customer customer, int quantity, int unitPrice)
        throws CustomerNotFoundException, OrderNotFoundException, StoreNotFoundException
    {
        int totalPrice = 0;
        List<Order> orders = getCustomerOrders(customer.getAccount());
        for (Order order: orders) {
            totalPrice += getOrderPrice(order.getStoreName(), order.getId());
        }
        return totalPrice + (unitPrice * quantity) <= customer.getCredit();
    }

    public int getDroneOrderSize(String storeName, String droneId) {
        Long size = listOperations.size(DRONEORDER + ":" + storeName + ":" + droneId);
        if (size == null) {
            return 0;
        } else {
            return size.intValue();
        }
    }
}
