package com.project.core.rest.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.core.domain.dto.RepliedMessage;
import com.project.core.domain.enums.MessageStateEnum;
import com.project.core.domain.enums.MessageTypeEnum;
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
