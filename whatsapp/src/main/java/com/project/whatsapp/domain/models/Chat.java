package com.project.whatsapp.domain.models;

import com.project.whatsapp.domain.enums.MessageTypeEnum;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
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
public class Chat extends BaseModel implements Persistable<String> {
    @MongoId
    @Field(value = "_id")
    private String id = UUID.randomUUID().toString();
    @Field(value = "name")
    private String name;
    @Field(value = "is_group_chat", targetType = FieldType.BOOLEAN)
    private boolean isGroupChat = false;
    @Field(value = "group_chat_mode")
    private int groupChatMode;
    @Field(value = "description")
    private String description;
    @Field(value = "chat_image_url")
    private String chatImageUrl;
    @Field(value = "last_message")
    private Message lastMessage;
    @Field(value = "user_ids")
    private List<String> userIds;
    @Transient
    private boolean isNew = false;

    public String getChatName(String userId) {
        if (isGroupChat) return name;
        else {
            return Arrays.stream(name.split("#")).filter(id ->
                    !id.startsWith(userId)).findFirst()
                .map(idWithName -> idWithName.split("&")[1]).orElseThrow();
        }
    }

    public String getChatImageUrl(String userId) {
        if (isGroupChat) return chatImageUrl;
        else {
            String userImageUrl = Arrays.stream(chatImageUrl.split("#")).filter(id ->
                    !id.startsWith(userId)).findFirst()
                .map(idWithUrl -> idWithUrl.split("&")[1]).orElseThrow();
            return userImageUrl.equals("null") ? null : userImageUrl;
        }
    }

    public String getLastMessage() {
        if (lastMessage == null) return "Chat created";
        if (lastMessage.getMessageType().equals(MessageTypeEnum.TEXT)) return lastMessage.getContent();
        return "Attachment";
    }

    public LocalDateTime getLastMessageTime() {
        if (lastMessage == null) return getCreatedAt();
        return lastMessage.getCreatedAt();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
