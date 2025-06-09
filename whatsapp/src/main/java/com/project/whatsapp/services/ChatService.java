package com.project.whatsapp.services;

import com.project.whatsapp.rest.outbound.ChatResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getChatsByReceiverId(String userId);
    String createChat(String senderId, List<String> receiversIds, boolean isGroupChat, String chatName);
}
