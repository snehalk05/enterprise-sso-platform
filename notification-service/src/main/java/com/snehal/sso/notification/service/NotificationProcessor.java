package com.snehal.sso.notification.service;

import com.snehal.sso.events.NotificationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NotificationProcessor {
    private static final Logger log = LoggerFactory.getLogger(NotificationProcessor.class);

    @Async("notificationExecutor")
    public CompletableFuture<Void> process(NotificationCommand command) {
        if (command.recipient() == null || command.recipient().isBlank()) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Notification recipient is required"));
        }
        log.info("Sending {} notification to {} with subject={}",
                command.channel(), command.recipient(), command.subject());
        return CompletableFuture.completedFuture(null);
    }
}
