package com.snehal.sso.audit;

import com.snehal.sso.events.BaseEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuditListener {
    private final AuditLogRepository repo;

    public AuditListener(AuditLogRepository repo) {
        this.repo = repo;
    }

    @KafkaListener(topics = "audit-events", groupId = "audit-service")
    public void on(BaseEvent event) {
        if (repo.existsById(event.eventId())) return;
        AuditLog log = new AuditLog();
        log.id = event.eventId();
        log.eventId = event.eventId();
        log.type = event.eventType();
        log.occurredAt = event.occurredAt();
        log.payload = event.payload();
        repo.save(log);
    }
}
