package com.project.whatsapp.rest.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.whatsapp.domain.dto.RepliedMessage;
import com.project.whatsapp.domain.enums.MessageStateEnum;
import com.project.whatsapp.domain.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    private Long id;
    private String content;
    private MessageTypeEnum type;
    private MessageStateEnum state;
    private String senderId;
    private LocalDateTime createdAt;
    private boolean isForwarded;
    private RepliedMessage repliedMessage;
    private List<String> mediaListReferences;
}
