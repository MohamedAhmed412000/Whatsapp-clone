package com.project.whatsapp.repositories;

import com.project.whatsapp.domain.models.ChatUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, UUID> {
    @Query("SELECT cu FROM ChatUser cu WHERE cu.chat.id = :chatId AND cu.user.id = :userId")
    Optional<ChatUser> findByChatIdAndUserId(UUID chatId, UUID userId);

    @Query("SELECT cu.lastSeenMessageAt FROM ChatUser cu WHERE cu.chat.id = :chatId " +
        "ORDER BY cu.lastSeenMessageAt ASC LIMIT 1")
    LocalDateTime findLastMessageViewedFromAllMembers(UUID chatId);
}
