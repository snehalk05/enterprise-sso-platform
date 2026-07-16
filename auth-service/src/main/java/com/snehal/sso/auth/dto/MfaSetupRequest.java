package com.snehal.sso.auth.dto;

import com.snehal.sso.auth.domain.MfaType;
import jakarta.validation.constraints.NotNull;

public record MfaSetupRequest(@NotNull MfaType type, String phone, String securityQuestion, String securityAnswer) {
}