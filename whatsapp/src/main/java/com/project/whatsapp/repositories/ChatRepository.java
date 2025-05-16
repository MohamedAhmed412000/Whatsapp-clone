package com.project.whatsapp.repositories;

import com.project.whatsapp.domain.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    @Query("SELECT c FROM Chat c " +
        "JOIN ChatUser cu ON cu.chat.id = c.id " +
        "WHERE cu.user.id = :senderId")
    List<Chat> findChatsBySenderId(UUID senderId);

    @Query("SELECT c FROM Chat c " +
        "JOIN ChatUser cu ON cu.chat.id = c.id " +
        "WHERE c.isGroupChat = false AND (cu.user.id = :senderId OR cu.user.id = :receiverId)")
    Optional<Chat> findChatsBySenderIdAndReceiverId(UUID senderId, UUID receiverId);

}
