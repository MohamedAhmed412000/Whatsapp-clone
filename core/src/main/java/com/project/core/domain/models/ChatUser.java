package com.project.core.domain.models;

import com.project.core.domain.enums.ChatUserRoleEnum;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_user")
public class ChatUser {
    @MongoId
    @Field(value = "_id")
    private String id = UUID.randomUUID().toString();
    @Field(value = "chat_id")
    private String chatId;
    @Field(value = "user_id")
    private String userId;
    @Field(value = "role")
    private ChatUserRoleEnum role;
    @Field(value = "last_seen_message_at", targetType = FieldType.TIMESTAMP)
    private LocalDateTime lastSeenMessageAt;
}
