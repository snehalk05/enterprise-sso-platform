package com.snehal.sso.events;

public record PasswordResetPayload(String userId, String email) {
}
