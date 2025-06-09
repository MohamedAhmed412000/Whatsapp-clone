package com.project.whatsapp.services;

import com.project.whatsapp.rest.inbound.MessageResource;
import com.project.whatsapp.rest.outbound.MessageResponse;

import java.util.List;

public interface MessageService {
    void saveMessage(MessageResource request);
    void setLastViewTime(String chatId, String userId);
    List<MessageResponse> findChatMessages(String chatId, int page, String userId);
}
