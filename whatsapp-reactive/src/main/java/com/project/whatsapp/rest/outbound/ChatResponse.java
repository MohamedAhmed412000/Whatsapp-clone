package com.project.whatsapp.rest.outbound;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatResponse {
    private String id;
    private String name;
    private long unreadCount;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private boolean isPrecipitantOnline;
    private String senderId;
    private String receiverId;
}
