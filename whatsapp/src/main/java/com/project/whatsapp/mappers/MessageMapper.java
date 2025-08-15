package com.project.whatsapp.mappers;

import com.project.whatsapp.domain.enums.MessageStateEnum;
import com.project.whatsapp.domain.models.Message;
import com.project.whatsapp.rest.outbound.MessageResponse;
import com.project.whatsapp.rest.outbound.StarredMessageResponse;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponse toMessageResponse(Message message, MessageStateEnum state) {
        return MessageResponse.builder()
            .id(message.getId())
            .content(message.getContent())
            .type(message.getMessageType())
            .state(state)
            .senderId(message.getSenderId())
            .createdAt(message.getCreatedAt())
            .isForwarded(message.isForwarded())
            .repliedMessage(message.getRepliedMessage())
            .mediaListReferences(message.getMediaReferencesList())
            .build();
    }

    public StarredMessageResponse toStarredMessageResponse(Message message) {
        return StarredMessageResponse.builder()
            .id(message.getId())
            .content(message.getContent())
            .type(message.getMessageType())
            .senderId(message.getSenderId())
            .createdAt(message.getCreatedAt())
            .mediaListReferences(message.getMediaReferencesList())
            .build();
    }

}
