package com.snehal.sso.events;
public record LoginPayload(String userId, String email, String ipAddress, boolean success) { }
