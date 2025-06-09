package com.project.gateway.services.impl;

import com.project.gateway.domain.models.User;
import com.project.gateway.mappers.UserMapper;
import com.project.gateway.repositories.UserRepository;
import com.project.gateway.services.UserSynchronizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSynchronizerImpl implements UserSynchronizer {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public void synchronizeWithIdp(Jwt token) {
        log.info("Synchronizing user with idp");
        getUserEmail(token).ifPresent(email -> {
            log.info("Synchronizing user with email: {}", email);
            User user = userMapper.fromTokenClaims(token.getClaims());
            userRepository.save(user);
        });
    }

    private Optional<String> getUserEmail(Jwt token) {
        Map<String, Object> claims = token.getClaims();
        if (claims.containsKey("email")) {
            return Optional.of(claims.get("email").toString());
        }
        return Optional.empty();
    }
}
