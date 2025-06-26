package com.project.gateway.services.impl;

import com.project.gateway.mappers.UserMapper;
import com.project.gateway.repositories.UserRepository;
import com.project.gateway.services.UserSynchronizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSynchronizerImpl implements UserSynchronizer {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public Mono<Void> synchronizeWithIdp(Jwt token) {
        return Mono.justOrEmpty(getUserEmail(token))
            .doOnNext(email -> log.info("Synchronizing user with email: {}", email))
            .map(email -> userMapper.fromTokenClaims(token.getClaims()))
            .flatMap(userRepository::upsertUser)
            .then();
    }

    private Optional<String> getUserEmail(Jwt token) {
        Map<String, Object> claims = token.getClaims();
        if (claims.containsKey("preferred_username")) {
            return Optional.of(claims.get("preferred_username").toString());
        }
        return Optional.empty();
    }
}