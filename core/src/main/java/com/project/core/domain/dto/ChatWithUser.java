package com.project.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.core.domain.models.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatWithUser {
    private Chat chat;
    private LocalDateTime lastSeen;
}
