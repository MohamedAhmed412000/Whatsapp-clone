package com.project.whatsapp.domain.models;

import com.project.whatsapp.domain.enums.MessageTypeEnum;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat")
public class Chat extends BaseModel {
    @MongoId
    @Field(value = "_id")
    private UUID id = UUID.randomUUID();
    @Field(value = "name")
    private String name;
    @Field(value = "is_group_chat", targetType = FieldType.BOOLEAN)
    private boolean isGroupChat = false;
    @Field(value = "description")
    private String description;
    @Field(value = "chat_image_url")
    private String chatImageUrl;
    @Field(value = "last_message")
    private Message lastMessage;
    @Field(value = "message_ids", targetType = FieldType.ARRAY)
    private List<UUID> messageIds;
    @Field(value = "user_ids", targetType = FieldType.ARRAY)
    private List<UUID> userIds;

    public String getChatName(UUID userId) {
        if (isGroupChat) return name;
        else {
            return Arrays.stream(name.split("#")).filter(id ->
                    !id.startsWith(userId.toString())).findFirst()
                .map(idWithName -> idWithName.split("&")[1]).orElseThrow();
        }
    }

    public String getChatImageUrl(UUID userId) {
        if (isGroupChat) return chatImageUrl;
        else {
            return Arrays.stream(chatImageUrl.split("#")).filter(id ->
                    !id.startsWith(userId.toString())).findFirst()
                .map(idWithUrl -> idWithUrl.split("&")[1]).orElseThrow();
        }
    }

    public String getLastMessage() {
        if (lastMessage.getMessageType().equals(MessageTypeEnum.TEXT)) return lastMessage.getContent();
        return "Attachment";
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessage.getCreatedAt();
    }
}
