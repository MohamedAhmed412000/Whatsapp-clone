package com.project.whatsapp.services;

import com.project.whatsapp.rest.inbound.MessageResource;
import com.project.whatsapp.rest.inbound.MessageUpdateResource;
import com.project.whatsapp.rest.outbound.MessageResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MessageService {
    void saveMessage(MessageResource request);
    Map<LocalDate, List<MessageResponse>> findChatMessages(String chatId, int page);
    boolean editMessage(Long messageId, MessageUpdateResource resource);
    boolean deleteMessage(Long messageId, boolean deleteForEveryone);
}
