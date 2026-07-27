package com.snehal.sso.auth.service;

import com.snehal.sso.auth.config.MessagingConfig;
import com.snehal.sso.events.BaseEvent;
import com.snehal.sso.events.NotificationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class EventPublisher {
    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);
    private static final String AUDIT_TOPIC = "audit-events";

    private final KafkaTemplate<String, BaseEvent> kafka;
    private final RabbitTemplate rabbit;

    public EventPublisher(KafkaTemplate<String, BaseEvent> kafka, RabbitTemplate rabbit) {
        this.kafka = kafka;
        this.rabbit = rabbit;
    }

    public CompletableFuture<SendResult<String, BaseEvent>> event(BaseEvent event) {
        return kafka.send(AUDIT_TOPIC, event.eventId(), event)
                .orTimeout(10, TimeUnit.SECONDS)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error("Kafka publish failed. eventId={}, topic={}",
                                event.eventId(), AUDIT_TOPIC, error);
                    } else {
                        log.info("Kafka publish succeeded. eventId={}, partition={}, offset={}",
                                event.eventId(), result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    @Async("messagingExecutor")
    public CompletableFuture<Void> notification(NotificationCommand command) {
        try {
            CorrelationData correlation = new CorrelationData(command.correlationId());
            rabbit.convertAndSend(
                    MessagingConfig.NOTIFICATION_EXCHANGE,
                    MessagingConfig.NOTIFICATION_ROUTING_KEY,
                    command,
                    correlation);
            log.info("RabbitMQ notification submitted. correlationId={}, recipient={}",
                    command.correlationId(), command.recipient());
            return CompletableFuture.completedFuture(null);
        } catch (RuntimeException error) {
            log.error("RabbitMQ notification publish failed. correlationId={}",
                    command.correlationId(), error);
            return CompletableFuture.failedFuture(error);
        }
    }
}
