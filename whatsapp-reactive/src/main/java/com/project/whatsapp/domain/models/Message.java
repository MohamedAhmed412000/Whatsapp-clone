package com.project.whatsapp.domain.models;

import com.project.whatsapp.domain.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Table("MESSAGE")
public class Message extends BaseModel {
    @Id
    private Long id;
    @Column("MESSAGE_CONTENT")
    private String message;
    @Column("CHAT_ID")
    private UUID chatId;
    @Column("SENDER_ID")
    private UUID senderId;
    @Column("TYPE")
    private MessageTypeEnum messageType;

    @Transient
    private Chat chat;
}
