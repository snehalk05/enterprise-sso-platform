package com.snehal.sso.auth.dto;

import java.util.Set;

public record AuthUserResponse(
        String id,
        String username,
        String email,
        Set<String> roles,
        boolean emailVerified,
        boolean locked,
        boolean mfaEnabled
) {
}
