package com.project.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.core.domain.models.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatWithUser {
    private Chat chat;
    private LocalDate lastSeen;
}
