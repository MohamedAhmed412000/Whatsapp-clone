package com.project.whatsapp.repositories;

import com.project.whatsapp.domain.models.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {
    @Query("SELECT * FROM USER WHERE USER.EMAIL = :email")
    Mono<User> findByEmail(String email);
}
