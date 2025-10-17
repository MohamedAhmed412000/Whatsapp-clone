package com.project.core.services;

import com.project.core.rest.inbound.GroupChatUpdateResource;
import com.project.core.rest.outbound.ChatResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatService {
    ChatResponse getChatDetails(String chatId);
    List<ChatResponse> getChatsByReceiverId();
    List<String> getSingleChatsUsers();
    String createOneToOneChat(String receiverId);
    String createGroupChat(String chatName, String description, MultipartFile groupChatProfileFile,
                           List<String> receiversIds);
    Boolean updateGroupChat(String chatId, GroupChatUpdateResource resource);
    Boolean deleteGroupChat(String chatId);
}
