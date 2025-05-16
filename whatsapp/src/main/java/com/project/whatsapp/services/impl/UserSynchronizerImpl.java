package com.project.whatsapp.services.impl;

import com.project.whatsapp.domain.models.User;
import com.project.whatsapp.mappers.UserMapper;
import com.project.whatsapp.repositories.UserRepository;
import com.project.whatsapp.services.UserSynchronizer;
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
