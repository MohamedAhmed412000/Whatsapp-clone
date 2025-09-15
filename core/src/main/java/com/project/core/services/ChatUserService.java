package com.project.core.services;

import com.project.core.rest.inbound.ChatUserUpdateResource;
import com.project.core.rest.outbound.ChatUserResponse;

import java.util.List;

public interface ChatUserService {
    List<ChatUserResponse> getChatUsers(String chatId);
    List<String> getChatUserIds(String chatId);
    Boolean updateGroupChatUsers(String chatId, ChatUserUpdateResource resource);
}
