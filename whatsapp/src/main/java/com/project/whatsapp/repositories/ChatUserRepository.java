package com.project.whatsapp.repositories;

import com.project.whatsapp.domain.models.ChatUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatUserRepository extends MongoRepository<ChatUser, String> {

    @Query(value = "{ 'chatId' : ?0, 'userId' : ?1 }")
    Optional<ChatUser> findByChatIdAndUserId(String chatId, String userId);

    @Query(value = "{ 'chatId' : ?0, 'userId' : { '$ne' : ?1 } }", fields = "userId")
    List<String> getOtherChatUserIds(String chatId, String senderId);

    void deleteByChatIdAndUserId(String chatId, String userId);
}
