package com.project.user.repositories;

import com.project.user.domain.models.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserContactRepository extends MongoRepository<Contact,Long> {
    @Query("{ '$or': [" +
            "{ 'user_id': ?0, 'owner_id': ?1, 'is_blocked': true }, " +
            "{ 'user_id': ?1, 'owner_id': ?0, 'is_blocked': true }" +
        "]}")
    Optional<Contact> findBlockedContactByUserId(String userId1, String userId2);

    @Query("{ 'owner_id': ?0, 'user_id': ?1 }")
    Optional<Contact> findContactByOwnerIdAndUserId(String ownerId, String userId);
}
