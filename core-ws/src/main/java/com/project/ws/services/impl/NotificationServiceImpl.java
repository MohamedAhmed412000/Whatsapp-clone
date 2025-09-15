package com.project.ws.services.impl;

import com.project.ws.constants.Application;
import com.project.ws.domain.dto.Notification;
import com.project.ws.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messageTemplate;

    @Override
    @RabbitListener(queues = Application.RABBITMQ_NOTIFICATIONS_QUEUE)
    public void handleNotification(Notification notification) {
        log.info("Received notification: {}", notification);
        List<String> receiverIds = notification.getReceiverIds();
        notification.setReceiverIds(null);
        receiverIds.stream().filter(receiverId -> !receiverId.equals(notification.getSenderId()))
            .forEach(receiverId -> messageTemplate
                .convertAndSendToUser(receiverId, "/chat", notification));
    }
}
