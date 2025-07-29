package com.project.whatsapp.services;

import com.project.whatsapp.rest.inbound.MessageResource;
import com.project.whatsapp.rest.outbound.MessageResponse;

import java.util.List;

public interface MessageService {
    void saveMessage(MessageResource request);
    List<MessageResponse> findChatMessages(String chatId, int page);
    boolean editMessage(Long messageId, String messageContent);
    boolean deleteMessage(Long messageId, boolean deleteForEveryone);
}
