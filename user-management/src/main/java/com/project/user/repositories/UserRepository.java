package com.project.user.repositories;

import com.project.user.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ '_id' : ?0 }")
    Optional<User> findByPublicId(String publicId);

    @Query("{ 'email' : ?0 }")
    Optional<User> findByEmail(String email);

    @Query(value = "{ '_id' : { $ne : ?0 } }", sort = "{ 'firstName' : 1, 'lastName' : 1 }")
    List<User> findAllUsersExceptSelf(String id);

    @Query(value = "{ '_id' : { $ne : ?0 }, 'first_name' : { $regex: '.*?1.*', $options: 'i' } }",
        sort = "{ 'firstName' : 1, 'lastName' : 1 }")
    List<User> findUsersExceptSelf(String id, String query);
}
