package com.project.core.domain.dto;

import com.project.core.domain.enums.MessageTypeEnum;
import com.project.core.domain.models.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
public class RepliedMessage {
    @Schema(description = "The source replying message id", example = "1755250373935")
    @Field(value = "_id", targetType = FieldType.INT64)
    private Long id;
    @Schema(description = "The message string content", example = "Hello")
    @Field(value = "content")
    private MessageContent content;
    @Schema(description = "The message content type", allowableValues = {
        "TEXT", "MEDIA", "AUDIO"
    })
    @Field(value = "message_type")
    private MessageTypeEnum messageType;

    public RepliedMessage(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.messageType = message.getMessageType();
    }
}
