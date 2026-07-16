package com.snehal.sso.security;

import java.util.Set;

public record JwtClaims(String subject, String email, Set<String> roles) {
}
