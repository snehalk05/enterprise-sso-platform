package com.snehal.sso.audit;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("audit_logs")
public class AuditLog {
    @Id
    public String id;
    public String eventId;
    public String type;
    public Instant occurredAt;
    public Object payload;
}
