package com.project.whatsapp.domain.dto;

import com.project.whatsapp.clients.dto.outbound.MediaContentResponse;
import com.project.whatsapp.domain.enums.MessageTypeEnum;
import com.project.whatsapp.domain.enums.NotificationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private String chatId;
    private String content;
    private String senderId;
    private String chatName;
    private MessageTypeEnum messageType;
    private NotificationTypeEnum notificationType;
    private List<MediaContentResponse> mediaList;
}
