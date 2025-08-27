package com.project.core.mappers;

import com.project.core.domain.enums.MessageStateEnum;
import com.project.core.domain.models.Message;
import com.project.core.rest.outbound.MessageResponse;
import com.project.core.rest.outbound.StarredMessageResponse;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponse toMessageResponse(Message message, MessageStateEnum state) {
        return MessageResponse.builder()
            .id(message.getId())
            .content(message.getContent().getContent())
            .type(message.getMessageType())
            .state(state)
            .senderId(message.getSenderId())
            .createdAt(message.getCreatedAt())
            .isForwarded(message.isForwarded())
            .repliedMessage(message.getRepliedMessage())
            .mediaListReferences(message.getContent().getMediaReferences())
            .build();
    }

    public StarredMessageResponse toStarredMessageResponse(Message message) {
        return StarredMessageResponse.builder()
            .id(message.getId())
            .content(message.getContent().getContent())
            .type(message.getMessageType())
            .senderId(message.getSenderId())
            .createdAt(message.getCreatedAt())
            .mediaListReferences(message.getContent().getMediaReferences())
            .build();
    }

}
