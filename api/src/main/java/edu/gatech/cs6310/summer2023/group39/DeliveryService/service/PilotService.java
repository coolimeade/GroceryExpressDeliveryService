package edu.gatech.cs6310.summer2023.group39.DeliveryService.service;

import java.util.HashSet;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.LicenseExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.PilotExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.PilotNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.DronePilot;

@Service
public class PilotService {
    private HashSet<String> licenses = new HashSet<String>();
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, DronePilot> hashOperations;

    @Autowired
    public PilotService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public void makePilot(String account, String firstName, String lastName, String phone, String taxId, String license, int experience) throws LicenseExistsException, PilotExistsException {
        if (hashOperations.hasKey("PILOT", account))
            throw new PilotExistsException(account);
        if (this.licenses.contains(license))
            throw new LicenseExistsException(license);
        hashOperations.put("PILOT", account, new DronePilot(account, firstName, lastName, phone, taxId, 0, 0, license, experience));
        this.licenses.add(license);
    }

    public void forEachPilot(Consumer<DronePilot> consumer) {
        hashOperations.values("PILOT").forEach(consumer);
    }

    public DronePilot getPilot(String account) throws PilotNotFoundException {
        if (!hashOperations.hasKey("PILOT", account)) {
            throw new PilotNotFoundException(account);
        }
        return hashOperations.get("PILOT", account);
    }

    public void setPilot(DronePilot pilot) {
        hashOperations.put("PILOT", pilot.getAccount(), pilot);
        return;
    }
}
