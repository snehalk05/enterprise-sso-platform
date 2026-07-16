package com.snehal.sso.auth.config;

import com.snehal.sso.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    JwtService jwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.access-ttl-seconds}") long ttl) {
        return new JwtService(secret, ttl);
    }
}
