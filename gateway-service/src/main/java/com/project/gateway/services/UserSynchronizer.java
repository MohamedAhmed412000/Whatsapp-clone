package com.project.gateway.services;

import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

public interface UserSynchronizer {
    Mono<Void> synchronizeWithIdp(Jwt token);
}
