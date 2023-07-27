package edu.gatech.cs6310.summer2023.group39.DeliveryService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.CustomerExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.CustomerNotFoundException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Customer;

@Service
public class CustomerService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Customer> hashOperations;

    @Autowired
    public CustomerService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public void makeCustomer(String account, String firstName, String lastName, String phoneNumber, int rating, int credit, int lat, int lon) throws CustomerExistsException {
        if (hashOperations.hasKey("USER", account))
            throw new CustomerExistsException(account);
        hashOperations.put("USER", account, new Customer(account, firstName, lastName, phoneNumber, rating, credit, lat, lon));
    }

    public void forEachCustomer(Consumer<Customer> consumer) {
        hashOperations.values("USER").forEach(consumer);
    }

    public Customer getCustomer(String account) throws CustomerNotFoundException {
        if (!hashOperations.hasKey("USER", account))
            throw new CustomerNotFoundException(account);
        return hashOperations.get("USER", account);
    }

    public void deductCredit(String account, int amount) throws CustomerNotFoundException {
        Customer customer = hashOperations.get("USER", account);
        if (customer == null) {
            throw new CustomerNotFoundException(account);
        }
        customer.deductCredit(amount);
        hashOperations.put("USER", account, customer);
    }
}
