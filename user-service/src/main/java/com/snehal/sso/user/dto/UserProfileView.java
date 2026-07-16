package com.snehal.sso.user.dto;

import com.snehal.sso.user.domain.UserProfile;

public record UserProfileView(
        UserProfile profile,
        AuthUserSummary account
) {
}
