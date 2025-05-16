package com.project.whatsapp.domain.models;

import com.project.whatsapp.domain.enums.MessageTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MESSAGE")
public class Message extends BaseModel {
    @Id
    @SequenceGenerator(name = "msg_seq", sequenceName = "msg_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msg_seq")
    private Long id;
    @Column(name = "MESSAGE_CONTENT")
    private String content;
    @ManyToOne
    @JoinColumn(name = "CHAT_ID")
    private Chat chat;
    @ManyToOne
    @JoinColumn(name = "SENDER_ID", nullable = false)
    private User sender;
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private MessageTypeEnum messageType;
}
