package com.project.whatsapp.filters;

import com.project.whatsapp.services.UserSynchronizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserSynchronizeFilter implements WebFilter {

    private final UserSynchronizeService userSynchronizeService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof
            AnonymousAuthenticationToken)) {
            JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
            userSynchronizeService.synchronizeWithKeycloak(token.getToken());
        }
        return chain.filter(exchange);
    }
}
