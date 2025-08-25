package com.project.core.repositories;

import com.project.core.domain.models.ChatUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatUserRepository extends MongoRepository<ChatUser, String> {

    @Query(value = "{ 'chat_id' : ?0, 'user_id' : ?1 }")
    Optional<ChatUser> findByChatIdAndUserId(String chatId, String userId);

    @Query(value = "{ 'chat_id' : ?0, 'user_id' : { '$ne' : ?1 } }", fields = "userId")
    List<String> getOtherChatUserIds(String chatId, String senderId);

}
