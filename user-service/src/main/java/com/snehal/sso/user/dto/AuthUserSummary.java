package com.snehal.sso.user.dto;

import java.util.Set;

public record AuthUserSummary(
        String id,
        String username,
        String email,
        Set<String> roles,
        boolean emailVerified,
        boolean locked,
        boolean mfaEnabled
) {
}
