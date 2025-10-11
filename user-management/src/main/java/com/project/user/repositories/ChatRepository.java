package com.project.user.repositories;

import com.project.user.domain.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

    @Query(value = "{ 'is_group_chat': false, 'user_ids': { '$all': [ ?0, ?1 ] } }")
    Optional<Chat> findChatsBySenderIdAndReceiverId(String senderId, String receiverId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'name': ?1 } }")
    void updateChatNameById(String id, String newName);
}