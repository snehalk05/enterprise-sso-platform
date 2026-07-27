package com.snehal.sso.auth.domain; import org.springframework.data.annotation.Id; import org.springframework.data.mongodb.core.mapping.Document; import java.time.Instant;
@Document("refresh_tokens") public class RefreshToken { @Id public String id; public String userId; public String token; public Instant expiresAt; public boolean revoked; }
