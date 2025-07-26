package com.project.whatsapp.rest.outbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponse {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private long unreadCount;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Boolean isRecipientOnline;
    private String senderId;
    private List<String> receiversId;
}
