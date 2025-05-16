package com.project.whatsapp.repositories;

import com.project.whatsapp.domain.models.Chat;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ChatRepository extends ReactiveCrudRepository<Chat, UUID> {

    @Query("SELECT c.* FROM CHAT c JOIN CHAT_USER cu ON cu.CHAT_ID = c.ID WHERE cu.USER_ID = :senderId")
    Flux<Chat> findAllBySenderId(UUID senderId);
}
