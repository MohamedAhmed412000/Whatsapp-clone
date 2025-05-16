package com.project.whatsapp.domain.models;

import com.project.whatsapp.domain.enums.MessageTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CHAT")
public class Chat extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "IS_GROUP")
    private boolean isGroupChat = false;
    @Column(name = "DESCRIPTION", nullable = true)
    private String description;
    @Column(name = "CHAT_IMAGE_URL", nullable = true)
    private String chatImageUrl;
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    private List<Message> messages;
    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    private List<ChatUser> users;

    @Transient
    public String getChatName(UUID userId) {
        if (isGroupChat) return name;
        else {
            if (users.get(0).getId().equals(userId))
                return users.get(1).getUser().getFullName();
            return users.get(0).getUser().getFullName();
        }
    }

    @Transient
    public long getUnreadMessageCount(UUID senderId) {
        return messages.stream()
            .filter(message -> message.getSender().getId().equals(senderId))
            .count();
    }

    @Transient
    public Boolean isRecipientOnline(UUID senderId) {
        if (this.isGroupChat) return null;
        else {
            if (users.get(0).getId().equals(senderId))
                return (Boolean) users.get(1).getUser().isOnlineUser();
            return (Boolean) users.get(0).getUser().isOnlineUser();
        }
    }

    @Transient
    public String getLastMessage() {
        if (messages != null && !messages.isEmpty()) {
            if (!messages.get(0).getMessageType().equals(MessageTypeEnum.TEXT)) return "Attachment";
            else return messages.get(0).getContent();
        }
        return null;
    }

    @Transient
    public LocalDateTime getLastMessageTime() {
        if (messages != null && !messages.isEmpty()) {
            return messages.get(0).getCreatedAt();
        }
        return null;
    }

    public Chat() {
        this.id = UUID.randomUUID();
    }
}
