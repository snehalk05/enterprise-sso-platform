package com.snehal.sso.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record RoleRequest(@NotBlank String email, @NotEmpty Set<String> roles) {
}