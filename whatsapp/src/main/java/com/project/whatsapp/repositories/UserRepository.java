package com.project.whatsapp.repositories;

import com.project.whatsapp.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
    @Query("{ 'email' : ?0 }")
    Optional<User> findByEmail(String email);

    @Query("{ '_id' : ?0 }")
    Optional<User> findByPublicId(UUID publicId);

    @Query("{ '_id' : { '$in' : ids } }")
    List<User> findByPublicIds(List<UUID> ids);

    @Query(value = "{ '_id' : { $ne : ?0 } }", sort = "{ 'firstName' : 1, 'lastName' : 1 }")
    List<User> findAllUsersExceptSelf(UUID id);
}
