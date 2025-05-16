package com.project.whatsapp.services;

import com.project.whatsapp.rest.outbound.ChatResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getChatsByReceiverId(Authentication authentication);
    String createChat(String senderId, List<String> receiversIds, boolean isGroupChat, String chatName);
}
