package com.snehal.sso.auth.controller;

import com.snehal.sso.auth.dto.*;
import com.snehal.sso.auth.service.AuthService;
import com.snehal.sso.auth.service.PasswordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    private final PasswordService passwords;

    public AuthController(AuthService auth, PasswordService passwords) {
        this.auth = auth;
        this.passwords = passwords;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(@Valid @RequestBody RegisterRequest r) {
        auth.register(r);
        return Map.of("message", "Registration successful. Verify your email before using protected functionality.");
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest r) {
        return auth.login(r);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest r) {
        return auth.refresh(r);
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody RefreshRequest r) {
        auth.logout(r.refreshToken());
    }

    @PostMapping("/mfa/{email}")
    public MfaSetupResponse setup(@PathVariable String email, @Valid @RequestBody MfaSetupRequest r) {
        return auth.setupMfa(email, r);
    }

    @PostMapping("/password-reset/request")
    public Map<String, String> resetRequest(@Valid @RequestBody PasswordResetRequest r) {
        return Map.of("devResetToken", passwords.requestReset(r.email()));
    }

    @PostMapping("/password-reset/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reset(@Valid @RequestBody PasswordResetConfirm r) {
        passwords.confirm(r.token(), r.newPassword());
    }

    @PostMapping("/email/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verify(@Valid @RequestBody VerifyEmailRequest r) {
        passwords.verifyEmail(r.token());
    }

    @PostMapping("/admin/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void roles(@Valid @RequestBody RoleRequest r) {
        auth.assignRoles(r);
    }
}
