package com.project.core.mappers;

import com.project.core.domain.models.Chat;
import com.project.core.domain.models.User;
import com.project.core.rest.outbound.ChatResponse;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.List;

import static com.project.core.constants.Application.LAST_ACTIVE_INTERVAL_IN_MINUTES;

@Component
public class ChatMapper {

    public ChatResponse toChatResponse(Chat chat, String senderId, long unreadMessageCount,
                                       User user, LocalDateTime lastSeen) {
        return ChatResponse.builder()
            .id(chat.getId())
            .name(chat.isGroupChat()? chat.getChatName(senderId): user.getFullName())
            .description(chat.isGroupChat()? chat.getDescription(): user.getDescription())
            .isGroupChat(chat.isGroupChat())
            .chatImageReference(chat.isGroupChat()? chat.getChatImageReference(senderId):
                user.getProfilePictureReference())
            .unreadCount(unreadMessageCount)
            .isRecipientOnline(isSelfChat(chat, senderId) || isOnlineUser(lastSeen))
            .lastMessage(chat.getLastMessage())
            .lastMessageTime(chat.getLastMessageTime())
            .senderId(senderId)
            .receiversId(isSelfChat(chat, senderId)? List.of(senderId): chat.getUserIds().stream()
                .filter(userId -> !userId.equals(senderId)).toList())
            .build();
    }

    private boolean isSelfChat(Chat chat, String senderId) {
        return chat.getUserIds().stream().allMatch(userId -> userId.equals(senderId));
    }

    private boolean isOnlineUser(LocalDateTime lastSeen) {
        // last seen => 10:05
        // now (10:09) => active
        // now (10:12) => offline
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now(Clock.systemUTC())
            .minusMinutes(LAST_ACTIVE_INTERVAL_IN_MINUTES));
    }
}
