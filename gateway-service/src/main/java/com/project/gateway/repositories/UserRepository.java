package com.project.gateway.repositories;

import com.project.gateway.domain.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, UUID> {
}
