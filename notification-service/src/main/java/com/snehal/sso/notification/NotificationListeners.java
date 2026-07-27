package com.snehal.sso.notification;

import com.snehal.sso.events.BaseEvent;
import com.snehal.sso.events.NotificationCommand;
import com.snehal.sso.notification.config.MessagingConfig;
import com.snehal.sso.notification.service.NotificationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionException;

@Component
public class NotificationListeners {
    private static final Logger log = LoggerFactory.getLogger(NotificationListeners.class);
    private final NotificationProcessor processor;

    public NotificationListeners(NotificationProcessor processor) {
        this.processor = processor;
    }

    @KafkaListener(topics = "audit-events", groupId = "notification-service")
    public void event(BaseEvent event) {
        if ("USER_REGISTERED".equals(event.eventType())) {
            log.info("Welcome notification required for eventId={}", event.eventId());
        }
    }

    @RabbitListener(queues = MessagingConfig.QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    public void command(NotificationCommand command) {
        try {
            processor.process(command).join();
        } catch (CompletionException error) {
            throw new IllegalStateException("Notification processing failed", error.getCause());
        }
    }

    @RabbitListener(queues = MessagingConfig.DLQ)
    public void deadLetter(NotificationCommand command) {
        log.error("Notification moved to DLQ. correlationId={}, recipient={}",
                command.correlationId(), command.recipient());
    }
}
