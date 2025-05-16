package com.project.whatsapp.services;

import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

public interface UserSynchronizeService {
    Mono<Void> synchronizeWithKeycloak(Jwt token);
}
