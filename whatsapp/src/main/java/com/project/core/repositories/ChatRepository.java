package com.project.core.repositories;

import com.project.core.domain.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

    @Query(value = "{ 'isGroupChat': false, 'userIds': { '$all': [ ?0, ?1 ] } }")
    Optional<Chat> findChatsBySenderIdAndReceiverId(String senderId, String receiverId);
}