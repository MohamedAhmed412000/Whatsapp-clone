package com.project.whatsapp.mappers;

import com.project.whatsapp.clients.dto.outbound.MediaContentResponse;
import com.project.whatsapp.domain.enums.MessageStateEnum;
import com.project.whatsapp.domain.models.Message;
import com.project.whatsapp.rest.outbound.MessageResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class MessageMapper {

    public MessageResponse toMessageResponse(Message message, MessageStateEnum state,
                                             List<MediaContentResponse> mediaList) {
        return MessageResponse.builder()
            .id(message.getId())
            .content(message.getContent())
            .type(message.getMessageType())
            .state(state)
            .senderId(message.getSender().getId().toString())
            .createdAt(message.getCreatedAt())
            .mediaList(mediaList)
            .build();
    }

    public MediaContentResponse toMediaResponse(MultipartFile multipartFile)
        throws IOException {
        return MediaContentResponse.builder()
            .name(multipartFile.getOriginalFilename())
            .size(multipartFile.getSize())
            .data(multipartFile.getBytes())
            .build();
    }

}
