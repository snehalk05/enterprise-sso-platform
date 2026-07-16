package com.snehal.sso.user.client;

import com.snehal.sso.user.dto.AuthUserSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    @GetMapping("/internal/users/{id}")
    AuthUserSummary getUserAccount(@PathVariable("id") String id);
}
