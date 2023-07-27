package edu.gatech.cs6310.summer2023.group39.DeliveryService.queue;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.lang.Nullable;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.DeliveryServiceException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MessageSubscriber implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageSubscriber.class);

    OrderService orderService;
    private MessagePublisher publisher;

    @Autowired
    public MessageSubscriber(OrderService orderService, MessagePublisher publisher) {
        this.orderService = orderService;
        this.publisher = publisher;
    }

    @Override
    public void onMessage(Message message, @Nullable byte[] pattern)
    {
        try {
            logger.debug("Message received: " + message.toString());
            String[] params = message.toString().split(":", 2);
            logger.debug("Params: " + params[0] + " " + params[1]);
            orderService.purchaseOrder(params[0], params[1]);
        }
        catch (DeliveryServiceException dse) {
            // On a domain exception, log it
            logger.error(dse.getMessage());
        }
        catch (Exception e) {
            // On any other exception, republish the message
            logger.error(e.getMessage());
            logger.debug("Republishing message: " + message.toString());
            publisher.publish(message.toString());
        }
    }
}
