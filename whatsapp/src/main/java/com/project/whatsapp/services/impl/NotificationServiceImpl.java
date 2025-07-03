package com.project.whatsapp.services.impl;

import com.project.whatsapp.domain.dto.Notification;
import com.project.whatsapp.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messageTemplate;

    @Override
    public void sendNotification(List<UUID> receiverIds, Notification notification) {
        log.info("Sending ws notification to {} with payload {}", receiverIds, notification);
        receiverIds.forEach(receiverId -> {
           messageTemplate.convertAndSendToUser(
               receiverId.toString(),
               "/chat",
               notification
           );
        });
    }
}
