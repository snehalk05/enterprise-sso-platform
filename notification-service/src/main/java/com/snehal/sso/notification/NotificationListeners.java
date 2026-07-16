package com.snehal.sso.notification;

import com.snehal.sso.events.BaseEvent;
import com.snehal.sso.events.NotificationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListeners {
    private static final Logger log = LoggerFactory.getLogger(NotificationListeners.class);

    @KafkaListener(topics = "audit-events", groupId = "notification-service")
    public void event(BaseEvent e) {
        if ("USER_REGISTERED".equals(e.eventType())) log.info("Would send welcome email for event={}", e.eventId());
    }

    @RabbitListener(queues = "notification.email")
    public void command(NotificationCommand c) {
        log.info("Would send {} to {} subject={}", c.channel(), c.recipient(), c.subject());
    }
}