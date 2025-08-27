package com.project.gateway.utils;

import com.project.gateway.exceptions.TokenElementExtractionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final ReactiveJwtDecoder jwtDecoder;

    public String extractToken(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new TokenElementExtractionException("Invalid authorization header");
        }
        return bearerToken.substring(7);
    }
    
    public Mono<String> extractSubject(String token) {
    return jwtDecoder.decode(token)
        .map(Jwt::getSubject)
        .switchIfEmpty(Mono.error(new TokenElementExtractionException("JWT token has no subject claim")));
}

    public Mono<Map<String, Object>> extractClaims(String token) {
        return jwtDecoder.decode(token)
            .map(Jwt::getClaims)
            .switchIfEmpty(Mono.error(new TokenElementExtractionException("JWT token has no claims")));
    }
}