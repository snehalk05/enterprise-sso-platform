package com.snehal.sso.auth.dto;

public record MfaSetupResponse(String type, String secret, String qrUri, String message) {
}