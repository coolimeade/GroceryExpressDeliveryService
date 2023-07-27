# Persistence

This document tells you how to move some data into the database.

# Steps

1. Pick part of the DeliveryService interface to
move to a separate service. For example, I moved all the stuff about getting customers to `CustomerService`. It's good to take all the methods that use a given `TreeMap`.

2. Create a new service class for that part of the interface. For example, I created `CustomerService`. It needs to use the @Service annotation.

3. Include code like this for the constructor:

```java
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Customer> hashOperations;

    @Autowired
    public CustomerService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }
```

Use your own class name for the constructor name!

4. Pick a prefix for your namespace. I used "USER"
since the user account ID is supposed to be unique.

5. Pick a key for your data. I used the customer
account number; it's supposed to be unique.

6. Use the hashOperations object instead of the TreeMap. Some simple mappings:

| TreeMap | hashOperations |
|---------|----------------|
| put     | put            |
| get     | get            |
| remove  | delete         |
| containsKey | hasKey     |
| forEach | values().forEach |

7. The model class (`Customer` in my case) needs to implement `Serializable`. Also, every property it has has to be serializable. I didn't want to
bother with the orders yet, so I just marked that `transient`, meaning it doesn't get persisted.

8. The controllers that use the service need to be updated to use the new service. For example, I changed `CustomerController` to use `CustomerService`.