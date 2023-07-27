package edu.gatech.cs6310.summer2023.group39.DeliveryService.service;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StationExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StationNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.RefuellingStation;

@Service
public class StationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, RefuellingStation> stationHashOperations;

    @Autowired
    public StationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stationHashOperations = this.redisTemplate.opsForHash();
    }

    public void makeRefuellingStation(String id, int lat, int lon) throws StationExistsException {
        if (stationHashOperations.hasKey("STATION", id))
            throw new StationExistsException(id);
        stationHashOperations.put("STATION", id, new RefuellingStation(id, lat, lon));
    }

    public void forEachRefuellingStation(Consumer<RefuellingStation> consumer) {
        stationHashOperations.values("STATION").forEach(consumer);
    }

    public void moveRefuellingStation(String id, int lat, int lon) throws StationNotFoundException {
        RefuellingStation station = stationHashOperations.get("STATION", id);
        if (station == null)
            throw new StationNotFoundException(id);
        station.setLocation(lat, lon);
        stationHashOperations.put("STATION", id, new RefuellingStation(id, lat, lon));
    }
}
