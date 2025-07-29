package com.project.whatsapp.repositories;

import com.project.whatsapp.domain.models.StarredMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StarredMessageRepository extends MongoRepository<StarredMessage, String> {
    @Query("{ 'user_id': ?0, 'message_id': ?1 }")
    Optional<StarredMessage> findStarredMessageByUserIdAndMessageId(String userId, Long messageId);
}
