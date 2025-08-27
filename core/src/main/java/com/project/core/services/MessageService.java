package com.project.core.services;

import com.project.core.rest.inbound.MessageResource;
import com.project.core.rest.inbound.MessageUpdateResource;
import com.project.core.rest.outbound.MessageResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MessageService {
    void saveMessage(MessageResource request);
    Map<LocalDate, List<MessageResponse>> findChatMessages(String chatId, int page);
    boolean editMessage(Long messageId, MessageUpdateResource resource);
    boolean deleteMessage(Long messageId, boolean deleteForEveryone);
}
