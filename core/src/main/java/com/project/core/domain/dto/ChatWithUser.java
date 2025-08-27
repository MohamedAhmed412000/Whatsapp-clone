package com.project.core.domain.dto;

import com.project.core.domain.models.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatWithUser {
    private Chat chat;
    private LocalDateTime lastSeen;
}
