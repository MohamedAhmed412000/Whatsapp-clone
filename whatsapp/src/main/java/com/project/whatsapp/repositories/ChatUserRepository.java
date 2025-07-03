package com.project.whatsapp.repositories;

import com.project.whatsapp.domain.models.ChatUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatUserRepository extends MongoRepository<ChatUser, UUID> {

    @Query(value = "{ 'chatId' : ?0, 'userId' : ?1 }")
    Optional<ChatUser> findByChatIdAndUserId(UUID chatId, UUID userId);

    @Query(value = "{ 'chatId' : ?0, 'userId' : { '$ne' : ?1 } }", fields = "userId")
    List<UUID> getOtherChatUserIds(UUID chatId, UUID senderId);

}
