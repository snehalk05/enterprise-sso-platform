package com.snehal.sso.gateway.filter;

import com.snehal.sso.security.JwtClaims;
import com.snehal.sso.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {
    private final JwtService jwtService;

    public JwtGatewayFilter(@Value("${app.jwt.secret}") String secret) {
        this.jwtService = new JwtService(secret, 900);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (path.startsWith("/api/auth") || path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            JwtClaims claims = jwtService.parse(header.substring(7));
            ServerWebExchange updatedExchange = exchange.mutate()
                    .request(request -> request
                            .header("X-User-Id", claims.subject())
                            .header("X-User-Email", claims.email())
                            .header("X-Roles", String.join(",", claims.roles())))
                    .build();
            return chain.filter(updatedExchange);
        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
