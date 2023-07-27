package edu.gatech.cs6310.summer2023.group39.DeliveryService.service;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Efficiency;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.GroceryStore;

@Service
public class EfficiencyService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Efficiency> hashOperations;

    @Autowired
    public EfficiencyService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public Efficiency getEfficiency(GroceryStore store) {
        if (!hashOperations.hasKey("EFFICIENCY", store.getName())) {
            Efficiency efficiency = new Efficiency(store);
            return efficiency;
        }
        else {
            Efficiency efficiency = hashOperations.get("EFFICIENCY", store.getName());
            return efficiency;
        }
    }

    public void forEachEfficiency(Consumer<Efficiency> consumer) {
        hashOperations.values("EFFICIENCY").forEach(consumer);
    }    

    public void saveEfficiency(String storeName, Efficiency efficiency)  {
        hashOperations.put("EFFICIENCY", storeName, efficiency);
    }
}
