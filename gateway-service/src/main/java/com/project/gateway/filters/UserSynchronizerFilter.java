package com.project.gateway.filters;

import com.project.gateway.services.UserSynchronizer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserSynchronizerFilter implements GlobalFilter {
    private final UserSynchronizer userSynchronizer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
            .filter(principal -> !(principal instanceof AnonymousAuthenticationToken))
            .cast(JwtAuthenticationToken.class)
            .map(token -> {
                userSynchronizer.synchronizeWithIdp(token.getToken());
                return token;
            })
            .then(chain.filter(exchange));
//            .switchIfEmpty(chain.filter(exchange));
    }
}