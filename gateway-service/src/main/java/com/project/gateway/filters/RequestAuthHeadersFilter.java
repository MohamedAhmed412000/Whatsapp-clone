package com.project.gateway.filters;

import com.project.gateway.constants.Headers;
import com.project.gateway.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Order(1)
@Component
@RequiredArgsConstructor
public class RequestAuthHeadersFilter implements GlobalFilter {
    private static final List<String> PUBLIC_PATHS = Arrays.asList("/core", "/core/", "/");
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        System.out.println(path);
        if (PUBLIC_PATHS.stream().anyMatch(path::equals)) {
            return chain.filter(exchange);
        }

        ServerWebExchange mutatedExchange = extractUserData(exchange);
        return chain.filter(mutatedExchange);
    }

    private ServerWebExchange extractUserData(ServerWebExchange exchange) {
        String token = jwtTokenUtil.extractToken(exchange);
        String userId = jwtTokenUtil.extractSubject(token);
        String userEmail = jwtTokenUtil.extractClaims(token).get("email").toString();
        return addUserHeadersToRequest(exchange, userId, userEmail);
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

}
