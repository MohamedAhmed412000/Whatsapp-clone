package com.project.core.repositories;

import com.project.core.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ '_id' : ?0 }")
    Optional<User> findByPublicId(String publicId);
}
