package com.snehal.sso.user.dto;

import java.util.Map;

public record ProfileUpdateRequest(String phone, String address, Map<String, String> preferences) {
}