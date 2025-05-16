package com.project.whatsapp.domain.models;

import com.project.whatsapp.domain.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Table("CHAT")
public class Chat extends BaseModel {
    @Id
    private UUID id;
    @Column("NAME")
    private String name;
    @Column("IS_GROUP")
    private boolean isGroupChat = false;
    @Column("DESCRIPTION")
    private String description;
    @Column("CHAT_IMAGE_URL")
    private String chatImageUrl;

    @Transient
    private List<User> users;

    @Transient
    private List<Message> messages;

    @Transient
    public String getChatName(UUID userId) {
        if (isGroupChat) return name;
        else {
            if (users.get(0).getId().equals(userId))
                return users.get(1).getFullName();
            return users.get(0).getFullName();
        }
    }

    @Transient
    public String getLastMessage() {
        if (!messages.get(0).getMessageType().equals(MessageTypeEnum.TEXT)) return "Attachment";
        else return messages.get(0).getMessage();
    }

    public Chat() {
        this.id = UUID.randomUUID();
    }
}
