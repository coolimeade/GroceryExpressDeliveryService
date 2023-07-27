package edu.gatech.cs6310.summer2023.group39.DeliveryService.config;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.queue.MessageSubscriber;

import javax.annotation.PostConstruct;
import org.springframework.cache.annotation.EnableCaching;

@Configuration
@EnableCaching
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @PostConstruct
    public void logDatabaseDetails() {
        logger.info("Connecting to Redis database at host " + redisHost + " and port " + redisPort);
    }

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(redisHost);
		config.setPort(redisPort);
		return new JedisConnectionFactory(config);
	}

	@Bean
    public ChannelTopic topic() {
        return new ChannelTopic("queue");
    }

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return redisTemplate;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(MessageSubscriber subscriber) {
    	return new MessageListenerAdapter(subscriber, "onMessage");
	}

	@Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("queue"));
        return container;
    }
}