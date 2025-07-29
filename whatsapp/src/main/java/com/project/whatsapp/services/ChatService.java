package com.project.whatsapp.services;

import com.project.whatsapp.rest.inbound.GroupChatUpdateResource;
import com.project.whatsapp.rest.outbound.ChatResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getChatsByReceiverId();
    String createOneToOneChat(String receiverId);
    String createGroupChat(String chatName, String chatImageUrl, String description, List<String> receiversIds);
    Boolean updateGroupChat(String chatId, GroupChatUpdateResource resource);
    Boolean deleteGroupChat(String chatId);
}
