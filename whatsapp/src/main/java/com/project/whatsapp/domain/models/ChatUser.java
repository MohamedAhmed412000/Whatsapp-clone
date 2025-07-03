package com.project.whatsapp.domain.models;

import com.project.whatsapp.domain.enums.ChatUserRoleEnum;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_user")
public class ChatUser extends BaseModel {
    @MongoId
    @Field(value = "_id")
    private UUID id = UUID.randomUUID();
    @Field(value = "chat_id")
    private UUID chatId;
    @Field(value = "user_id")
    private UUID userId;
    @Field(value = "role")
    private ChatUserRoleEnum role;
    @Field(value = "last_seen_message_at", targetType = FieldType.TIMESTAMP)
    private LocalDateTime lastSeenMessageAt;
}
