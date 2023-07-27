package edu.gatech.cs6310.summer2023.group39.DeliveryService.service;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.ItemExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.ItemNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StoreExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StoreNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.GroceryStore;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.StoreItem;

@Service
public class GroceryStoreService {

    private static final Logger logger = LoggerFactory.getLogger(GroceryStoreService.class);

    private final String TYPE = "STORE";
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, GroceryStore> hashOperations;

    @Autowired
    public GroceryStoreService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public GroceryStore makeStore(String name, int revenue, int lat, int lon) throws StoreExistsException {
        if (hashOperations.hasKey(TYPE, name))
            throw new StoreExistsException(name);
        logger.info("Making store: " + name);
        GroceryStore store = new GroceryStore(name, revenue, lat, lon);
        hashOperations.put(TYPE, name, store);
        return store;
    }

    public void forEachStore(Consumer<GroceryStore> consumer) {
        hashOperations.values(TYPE).forEach(consumer);
    }

    public GroceryStore getStore(String name) throws StoreNotFoundException {
        if (!hashOperations.hasKey(TYPE, name))
            throw new StoreNotFoundException(name);
        return hashOperations.get(TYPE, name);
    }

    public boolean hasStore(String name) {
        return hashOperations.hasKey(TYPE, name);
    }

    public void sellItem(String storeName, String itemName, int weight)
            throws ItemExistsException, StoreNotFoundException
    {
        GroceryStore store = getStore(storeName);
        store.sellItem(itemName, weight);
        hashOperations.put(TYPE, storeName, store);
    }

    public void forEachItem(String storeName, Consumer<StoreItem> consumer)
        throws StoreNotFoundException
    {
        GroceryStore store = getStore(storeName);
        store.forEachItem(consumer);
    }

    public StoreItem getItem(String storeName, String itemName)
        throws StoreNotFoundException, ItemNotFoundException
    {
        GroceryStore store = getStore(storeName);
        return store.getItem(itemName);
    }

    public void bookRevenue(String storeName, int totalPrice)
        throws StoreNotFoundException
    {
        GroceryStore store = getStore(storeName);
        store.bookRevenue(totalPrice);
        hashOperations.put(TYPE, storeName, store);
    }
}
