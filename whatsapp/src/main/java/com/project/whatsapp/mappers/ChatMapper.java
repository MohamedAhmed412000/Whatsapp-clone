package com.project.whatsapp.mappers;

import com.project.whatsapp.domain.models.Chat;
import com.project.whatsapp.rest.outbound.ChatResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.project.whatsapp.constants.Application.LAST_ACTIVE_INTERVAL_IN_MINUTES;

@Component
public class ChatMapper {

    public ChatResponse toChatResponse(Chat chat, UUID senderId, long unreadMessageCount,
                                       LocalDateTime lastSeen) {
        return ChatResponse.builder()
            .id(chat.getId())
            .name(chat.getChatName(senderId))
            .imageUrl(chat.getChatImageUrl(senderId))
            .unreadCount(unreadMessageCount)
            .isRecipientOnline(isOnlineUser(lastSeen))
            .lastMessage(chat.getLastMessage())
            .lastMessageTime(chat.getLastMessageTime())
            .senderId(senderId.toString())
            .receiversId(chat.getUserIds().stream().filter(userId -> !userId.equals(senderId))
                .map(UUID::toString).toList())
            .build();
    }

    private boolean isOnlineUser(LocalDateTime lastSeen) {
        // last seen => 10:05
        // now (10:09) => active
        // now (10:12) => offline
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(
            LAST_ACTIVE_INTERVAL_IN_MINUTES));
    }
}
