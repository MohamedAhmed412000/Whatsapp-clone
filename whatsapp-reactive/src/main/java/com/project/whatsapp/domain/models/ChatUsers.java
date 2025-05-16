package com.project.whatsapp.domain.models;

import com.project.whatsapp.domain.enums.ChatUserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Table("CHAT_USER")
public class ChatUsers extends BaseModel {
    @Id
    private UUID id;
    @Column("CHAT_ID")
    private UUID chatId;
    @Column("USER_ID")
    private UUID userId;
    @Column("USER_ROLE")
    private ChatUserRoleEnum role;
    @Column("LAST_REACHED_MESSAGE_TIME")
    private LocalDateTime lastReachedMessageAt;
    @Column("LAST_SEEN_MESSAGE_AT")
    private LocalDateTime lastSeenMessageAt;

    public ChatUsers() {
        this.id = UUID.randomUUID();
    }
}
