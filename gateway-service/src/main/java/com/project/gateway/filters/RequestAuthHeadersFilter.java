package com.project.gateway.filters;

import com.project.gateway.constants.Headers;
import com.project.gateway.utils.ReactiveJwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class RequestAuthHeadersFilter implements GlobalFilter {
    private static final List<String> PUBLIC_PATHS = List.of("/v3/api-docs");
    private final ReactiveJwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        HttpMethod method = exchange.getRequest().getMethod();
        log.debug("Processing request path: {}", path);
        
        if (PUBLIC_PATHS.contains(path) || checkMediaViewUrl(method, path)) {
            return chain.filter(exchange);
        }
        
        return extractAuthHeader(exchange)
            .flatMap(chain::filter)
            .onErrorResume(IllegalArgumentException.class, e -> 
                // Handle authentication errors
                Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
            )
            .onErrorResume(IllegalStateException.class, e ->
                // Handle missing claims errors
                Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST))
            );
    }

    private Mono<ServerWebExchange> extractUserData(ServerWebExchange exchange) {
        try {
            String token = jwtTokenUtil.extractToken(exchange);
            return Mono.zip(
                jwtTokenUtil.extractSubject(token),
                jwtTokenUtil.extractClaims(token)
            ).map(tuple -> {
                String userId = tuple.getT1();
                String userEmail = Optional.ofNullable(tuple.getT2().get("email"))
                    .map(Object::toString)
                    .orElseThrow(() -> new IllegalStateException("Email claim is missing"));
                return addUserHeadersToRequest(exchange, userId, userEmail);
            });
        } catch (IllegalArgumentException e) {
            return Mono.error(e);
        }
    }

    private Mono<ServerWebExchange> extractAuthHeader(ServerWebExchange exchange) {
        try {
            String authHeaderValue = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeaderValue == null || !authHeaderValue.startsWith("Bearer ")) {
                return Mono.error(() ->  new IllegalStateException("Authorization header is missing"));
            }
            return Mono.just(addAuthHeaderToRequest(exchange, authHeaderValue));
        }  catch (IllegalArgumentException e) {
            return Mono.error(e);
        }
    }

    private ServerWebExchange addUserHeadersToRequest (
        ServerWebExchange exchange, String userId, String userEmail
    ) {
        ServerHttpRequest mutatedRequest = exchange.getRequest()
            .mutate()
            .header(Headers.USER_ID, userId)
            .header(Headers.USER_EMAIL, userEmail)
            .build();
        return exchange.mutate().request(mutatedRequest).build();
    }

    private ServerWebExchange addAuthHeaderToRequest(
        ServerWebExchange exchange, String authHeaderValue
    ) {
        ServerHttpRequest mutatedRequest = exchange.getRequest()
            .mutate()
            .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
            .build();
        return exchange.mutate().request(mutatedRequest).build();
    }

    private boolean checkMediaViewUrl(HttpMethod method, String path) {
        return HttpMethod.GET.equals(method) && path.startsWith("/api/v1/media/");
    }

}