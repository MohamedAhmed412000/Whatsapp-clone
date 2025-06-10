package com.project.gateway.filters;

import com.project.gateway.services.UserSynchronizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.Principal;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserSynchronizerFilter implements GlobalFilter {
    private final UserSynchronizer userSynchronizer;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
            .filter(this::isAuthenticatedUser)
            .cast(JwtAuthenticationToken.class)
            .flatMap(token -> synchronizeUser(token)
                .then(Mono.just(true))
                .onErrorReturn(false))
            .defaultIfEmpty(true)
            .flatMap(result -> chain.filter(exchange))
            .onErrorResume(error -> {
                log.error("Filter chain error", error);
                return chain.filter(exchange);
            });
    }
    
    private boolean isAuthenticatedUser(Principal principal) {
        return !(principal instanceof AnonymousAuthenticationToken);
    }

    private Mono<Void> synchronizeUser(JwtAuthenticationToken token) {
        return userSynchronizer.synchronizeWithIdp(token.getToken())
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorResume(error -> {
                log.error("User synchronization failed", error);
                return Mono.empty();
            });
    }
}