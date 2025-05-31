package com.project.whatsapp.rest.outbound;

import com.project.whatsapp.domain.enums.MessageStateEnum;
import com.project.whatsapp.domain.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class MessageResponse {
    private Long id;
    private String content;
    private MessageTypeEnum type;
    private MessageStateEnum state;
    private String senderId;
    private LocalDateTime createdAt;
    private byte[] media;
}
