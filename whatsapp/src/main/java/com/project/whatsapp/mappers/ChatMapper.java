package com.project.whatsapp.mappers;

import com.project.whatsapp.domain.models.Chat;
import com.project.whatsapp.rest.outbound.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ChatMapper {

    public ChatResponse toChatResponse(Chat chat, UUID userId) {
        return ChatResponse.builder()
            .id(chat.getId())
            .name(chat.getChatName(userId))
            .unreadCount(chat.getUnreadMessageCount(userId))
            .isRecipientOnline(chat.isRecipientOnline(userId))
            .lastMessage(chat.getLastMessage())
            .lastMessageTime(chat.getLastMessageTime())
            .senderId(userId.toString())
            .receiversId(chat.getUsers().stream().map(chatUser -> chatUser.getUser().getId()
                .toString()).toList())
            .build();
    }

}
