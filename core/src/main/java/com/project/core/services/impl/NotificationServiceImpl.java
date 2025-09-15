package com.project.core.services.impl;

import com.project.core.constants.Application;
import com.project.core.domain.dto.Notification;
import com.project.core.services.ChatUserService;
import com.project.core.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final ChatUserService chatUserService;
    private final RabbitMessagingTemplate messageTemplate;

    @Async
    @Override
    public void sendNotificationAsync(Notification notification) {
        notification.setReceiverIds(chatUserService.getChatUserIds(notification.getChatId()));
        log.info("Sending notification to {} with id {}", notification.getReceiverIds(), notification.getChatId());
        messageTemplate.convertAndSend(Application.RABBITMQ_NOTIFICATIONS_QUEUE, notification);
    }
}
