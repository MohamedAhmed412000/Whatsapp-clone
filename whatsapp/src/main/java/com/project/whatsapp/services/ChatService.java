package com.project.whatsapp.services;

import com.project.whatsapp.rest.inbound.GroupChatUpdateResource;
import com.project.whatsapp.rest.outbound.ChatResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getChatsByReceiverId();
    String createOneToOneChat(String receiverId);
    String createGroupChat(String chatName, String description, MultipartFile groupChatProfileFile,
                           List<String> receiversIds);
    Boolean updateGroupChat(String chatId, GroupChatUpdateResource resource);
    Boolean deleteGroupChat(String chatId);
}
