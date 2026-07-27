package com.snehal.sso.events;
import java.util.Set;
public record RoleUpdatedPayload(String userId, Set<String> roles) { }
