package com.project.whatsapp.repositories;

import com.project.whatsapp.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ '_id' : ?0 }")
    Optional<User> findByPublicId(String publicId);

    @Query(value = "{ '_id' : { $ne : ?0 } }", sort = "{ 'firstName' : 1, 'lastName' : 1 }")
    List<User> findAllUsersExceptSelf(String id);

    @Query(value = "{ '_id' : { $ne : ?0 }, 'first_name' : { $regex: '.*?1.*', $options: 'i' } }",
        sort = "{ 'firstName' : 1, 'lastName' : 1 }")
    List<User> findUsersExceptSelf(String id, String query);
}
