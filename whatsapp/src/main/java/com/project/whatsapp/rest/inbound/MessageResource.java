package com.project.whatsapp.rest.inbound;

import com.project.whatsapp.clients.dto.inbound.MediaUploadResource;
import com.project.whatsapp.domain.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class MessageResource {
    private String content;
    private String senderId;
    private MessageTypeEnum messageType;
    private String chatId;
    private boolean isForwarded;
    private Long repliedMessageId;
    private List<MediaUploadResource> mediaResources;
}
