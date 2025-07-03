package com.project.whatsapp.domain.dto;

import com.project.whatsapp.domain.models.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatWithUser {
    private Chat chat;
    private LocalDateTime lastSeen;
}
