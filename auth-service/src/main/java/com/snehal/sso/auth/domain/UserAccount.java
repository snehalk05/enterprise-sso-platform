package com.snehal.sso.auth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Document("users")
public class UserAccount {
    @Id
    public String id;
    public String username;
    public String email;
    public String passwordHash;
    public Set<Role> roles = new HashSet<>(Set.of(Role.USER));
    public boolean emailVerified;
    public boolean locked;
    public int failedLoginAttempts;
    public Instant lockUntil;
    public MfaType mfaType = MfaType.NONE;
    public String totpSecret;
    public String passwordResetToken;
    public Instant passwordResetExpiresAt;
    public String emailVerificationToken;
    public Instant createdAt = Instant.now();
}
