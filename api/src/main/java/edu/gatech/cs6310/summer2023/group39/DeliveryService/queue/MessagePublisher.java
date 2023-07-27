package edu.gatech.cs6310.summer2023.group39.DeliveryService.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisher {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    private RedisTemplate<String, String> redisTemplate;
    private ChannelTopic topic;

    @Autowired
    public MessagePublisher(
        RedisTemplate<String, String> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(String message) {
        logger.debug("Publishing message: " + message + " to topic: " + topic.getTopic());
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
