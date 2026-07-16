package com.snehal.sso.events;

import java.time.Instant;
import java.util.UUID;

public record BaseEvent(String eventId, String eventType, String eventVersion, Instant occurredAt, String actorId,
                        String correlationId, Object payload) {
    public static BaseEvent of(EventType type, String actorId, String correlationId, Object payload) {
        return new BaseEvent(UUID.randomUUID().toString(), type.name(), "v1", Instant.now(), actorId, correlationId, payload);
    }
}
