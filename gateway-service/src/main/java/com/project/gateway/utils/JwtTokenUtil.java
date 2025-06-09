package com.project.gateway.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final ReactiveJwtDecoder jwtDecoder;

    public String extractToken(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        return bearerToken.substring(7);
    }
    
    public String extractSubject(String token) {
        Jwt jwtToken = jwtDecoder.decode(token).block();
        assert jwtToken != null;
        return jwtToken.getSubject();
    }

    public Map<String, Object> extractClaims(String token) {
        Jwt jwtToken = jwtDecoder.decode(token).block();
        assert jwtToken != null;
        return jwtToken.getClaims();
    }
}