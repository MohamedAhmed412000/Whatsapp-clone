package com.project.whatsapp.services;

import com.project.whatsapp.rest.inbound.ChatUserUpdateResource;
import com.project.whatsapp.rest.outbound.ChatUserResponse;

import java.util.List;

public interface ChatUserService {
    List<ChatUserResponse> getChatUsers(String chatId);
    Boolean updateGroupChatUsers(String chatId, ChatUserUpdateResource resource);
}
