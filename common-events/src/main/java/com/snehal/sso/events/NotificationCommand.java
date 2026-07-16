package com.snehal.sso.events;

public record NotificationCommand(String commandId, String channel, String recipient, String template, String subject,
                                  String body, String correlationId) {
}
