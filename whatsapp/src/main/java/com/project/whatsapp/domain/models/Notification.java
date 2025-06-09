package com.project.whatsapp.domain.models;

import com.project.whatsapp.clients.dto.outbound.MediaContentResponse;
import com.project.whatsapp.domain.enums.MessageTypeEnum;
import com.project.whatsapp.domain.enums.NotificationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private UUID chatId;
    private String content;
    private UUID senderId;
    private String chatName;
    private MessageTypeEnum messageType;
    private NotificationTypeEnum notificationType;
    private List<MediaContentResponse> mediaList;
}
