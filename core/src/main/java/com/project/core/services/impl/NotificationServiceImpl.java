package com.project.core.services.impl;

import com.project.core.domain.dto.Notification;
import com.project.core.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messageTemplate;

    @Override
    public void sendNotification(List<String> receiverIds, Notification notification) {
        log.info("Sending ws notification to {} with payload {}", receiverIds, notification);
        receiverIds.forEach(receiverId ->
            messageTemplate.convertAndSendToUser(receiverId, "/chat", notification));
    }
}
