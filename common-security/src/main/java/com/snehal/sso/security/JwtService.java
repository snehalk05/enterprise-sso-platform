package com.snehal.sso.security;
import io.jsonwebtoken.Claims; import io.jsonwebtoken.Jwts; import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey; import java.nio.charset.StandardCharsets; import java.time.Instant; import java.util.*;
public class JwtService {
    private final SecretKey key; private final long accessTtlSeconds;
    public JwtService(String secret, long accessTtlSeconds) { this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.accessTtlSeconds = accessTtlSeconds; }
    public String generateAccessToken(String subject, String email, Set<String> roles) { Instant now=Instant.now(); return Jwts.builder().subject(subject).claim("email", email).claim("roles", roles).issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(accessTtlSeconds))).signWith(key).compact(); }
    @SuppressWarnings("unchecked") public JwtClaims parse(String token) { Claims c=Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); Object raw=c.get("roles"); Set<String> roles=raw instanceof Collection<?> items ? items.stream().map(String::valueOf).collect(java.util.stream.Collectors.toSet()) : Set.of(); return new JwtClaims(c.getSubject(), c.get("email",String.class), roles); }
}
