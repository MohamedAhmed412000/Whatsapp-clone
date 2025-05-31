package com.project.whatsapp.mappers;

import com.project.whatsapp.domain.enums.MessageStateEnum;
import com.project.whatsapp.domain.models.Message;
import com.project.whatsapp.rest.outbound.MessageResponse;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponse toMessageResponse(Message message, MessageStateEnum state) {
        return MessageResponse.builder()
            .id(message.getId())
            .content(message.getContent())
            .type(message.getMessageType())
            .state(state)
            .senderId(message.getSender().getId().toString())
            .createdAt(message.getCreatedAt())
            // todo read the media file
            .build();
    }

}
