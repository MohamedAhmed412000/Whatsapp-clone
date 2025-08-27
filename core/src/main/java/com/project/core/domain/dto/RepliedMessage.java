package com.project.core.domain.dto;

import com.project.core.domain.enums.MessageTypeEnum;
import com.project.core.domain.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
public class RepliedMessage {
    @Field(value = "_id", targetType = FieldType.INT64)
    private Long id;
    @Field(value = "content")
    private MessageContent content;
    @Field(value = "message_type")
    private MessageTypeEnum messageType;

    public RepliedMessage(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.messageType = message.getMessageType();
    }
}
