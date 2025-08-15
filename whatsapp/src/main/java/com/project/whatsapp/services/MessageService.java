package com.project.whatsapp.services;

import com.project.whatsapp.rest.inbound.MessageResource;
import com.project.whatsapp.rest.outbound.MessageResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MessageService {
    void saveMessage(MessageResource request);
    Map<Date, List<MessageResponse>> findChatMessages(String chatId, int page);
    boolean editMessage(Long messageId, String messageContent);
    boolean deleteMessage(Long messageId, boolean deleteForEveryone);
}
