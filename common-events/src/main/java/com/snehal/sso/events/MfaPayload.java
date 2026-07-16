package com.snehal.sso.events;

public record MfaPayload(String userId, String email, String method) {
}
