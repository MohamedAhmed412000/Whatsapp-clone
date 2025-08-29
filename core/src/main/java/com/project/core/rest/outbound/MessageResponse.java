package com.project.core.rest.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.core.domain.dto.RepliedMessage;
import com.project.core.domain.enums.MessageStateEnum;
import com.project.core.domain.enums.MessageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
    name = "MessageResponse",
    description = "Schema to hold the message response"
)
@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    @Schema(description = "The message id", example = "1755250373935")
    private Long id;
    @Schema(description = "The message string content", example = "Hello")
    private String content;
    @Schema(description = "The message content type", allowableValues = {
        "TEXT", "MEDIA", "AUDIO"
    })
    private MessageTypeEnum type;
    @Schema(description = "The message state", allowableValues = {
        "SENT", "RECEIVED", "SEEN"
    })
    private MessageStateEnum state;
    @Schema(description = "The creator user id", example = "550e8400-e29b-41d4-a716-446655440000")
    private String senderId;
    @Schema(description = "The message creation time")
    private LocalDateTime createdAt;
    @Schema(description = "Flag to specify if the message is forwarded from another chat")
    private boolean isForwarded;
    private RepliedMessage repliedMessage;
    @Schema(description = "The message media list references")
    private List<String> mediaListReferences;
}
