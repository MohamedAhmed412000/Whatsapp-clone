package com.project.gateway.services.impl;

import com.project.gateway.domain.models.User;
import com.project.gateway.services.UserSynchronizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserSynchronizerImpl implements UserSynchronizer {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Void> synchronizeWithIdp(Jwt token) {
        return Mono.justOrEmpty(token)
            .doOnNext(jwt -> log.info("Synchronizing user with id: {}", token.getSubject()))
            .flatMap(jwt -> updateUserLastTimestamp(token));
    }

    private Mono<Void> updateUserLastTimestamp(Jwt token) {
        String userId = token.getSubject();
        Query query = new Query(Criteria.where("_id").is(userId));
        Update update = new Update().set("last_seen",
            LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return reactiveMongoTemplate.updateFirst(query, update, User.class).then();
    }
}