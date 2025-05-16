package com.project.whatsapp.repositories;

import com.project.whatsapp.domain.models.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, UUID> {
}
