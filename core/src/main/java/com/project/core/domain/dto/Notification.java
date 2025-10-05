package com.project.core.domain.dto;

import com.project.core.domain.enums.MessageTypeEnum;
import com.project.core.domain.enums.NotificationTypeEnum;
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
    private String id;
    private String chatId;
    private String content;
    private String senderId;
    private List<String> receiverIds;
    private String chatName;
    private MessageTypeEnum messageType;
    private NotificationTypeEnum notificationType;
    private List<String> mediaReferencesList;
}
