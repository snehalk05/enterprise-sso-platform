package com.snehal.sso.auth.service;

import com.snehal.sso.events.BaseEvent;
import com.snehal.sso.events.NotificationCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {
    private final KafkaTemplate<String, BaseEvent> kafka;
    private final RabbitTemplate rabbit;

    public EventPublisher(KafkaTemplate<String, BaseEvent> kafka, RabbitTemplate rabbit) {
        this.kafka = kafka;
        this.rabbit = rabbit;
    }

    public void event(BaseEvent e) {
        kafka.send("audit-events", e.eventId(), e);
    }

    public void notification(NotificationCommand c) {
        rabbit.convertAndSend("notification.exchange", "notification.email", c);
    }
}
