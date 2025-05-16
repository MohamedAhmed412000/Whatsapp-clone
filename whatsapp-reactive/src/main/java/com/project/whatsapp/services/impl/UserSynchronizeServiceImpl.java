package com.project.whatsapp.services.impl;

import com.project.whatsapp.domain.models.User;
import com.project.whatsapp.mappers.UserMapper;
import com.project.whatsapp.repositories.UserRepository;
import com.project.whatsapp.services.UserSynchronizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSynchronizeServiceImpl implements UserSynchronizeService {

    private final UserRepository userRepository;

    @Override
    public Mono<Void> synchronizeWithKeycloak(Jwt token) {
        User user = UserMapper.fromTokenClaims(token.getClaims());
        return userRepository.save(user).then();
    }

    private Optional<String> extractEmail(Jwt token) {
        Map<String, Object> claims = token.getClaims();
        if (claims.containsKey("email")) {
            return Optional.of(claims.get("email").toString());
        }
        return Optional.empty();
    }

}
