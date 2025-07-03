package com.project.whatsapp.domain.models;

import com.project.whatsapp.domain.enums.MessageTypeEnum;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "message")
public class Message extends BaseModel {
    @MongoId
    @Field(value = "_id", targetType = FieldType.INT64)
    private Long id;
    @Field(value = "content")
    private String content;
    @Field(value = "chat_id")
    private UUID chatId;
    @Field(value = "sender_id")
    private UUID senderId;
    @Field(value = "message_type")
    private MessageTypeEnum messageType;
}
