package com.project.ws.services.impl;

import com.project.commons.filters.dto.CustomAuthentication;
import com.project.ws.clients.CoreClient;
import com.project.ws.constants.Application;
import com.project.ws.domain.dto.Notification;
import com.project.ws.domain.enums.UserStatusEnum;
import com.project.ws.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final CoreClient coreClient;
    private final SimpMessagingTemplate messageTemplate;

    @RabbitListener(queues = Application.RABBITMQ_NOTIFICATIONS_QUEUE)
    public void handleNotification(Notification notification) {
        log.info("Received notification: {}", notification);
        List<String> receiverIds = notification.getReceiverIds();
        notification.setReceiverIds(null);
        receiverIds.stream().filter(receiverId -> !receiverId.equals(notification.getSenderId()))
            .forEach(receiverId -> messageTemplate
                .convertAndSend("/topic/chat." + receiverId, notification));
    }

    @Override
    public void handleUserStatus(Map<String,Object> messageBody) {
        String userId = messageBody.get("userId").toString();
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication(userId, ""));
        UserStatusEnum userStatus = UserStatusEnum.fromValue(messageBody.get("status").toString());

        if (userStatus != null) {
            List<String> receiverIds = coreClient.getSingleChatsUserIds().getBody();
            if (receiverIds != null) {
                Notification notification = Notification.builder()
                    .senderId(userId)
                    .notificationType(userStatus.getNotificationType())
                    .build();
                receiverIds.forEach(receiverId -> messageTemplate
                    .convertAndSend("/topic/chat." + receiverId, notification));
            }
        } else {
            log.info("UserStatus is null, Message: {}", messageBody);
        }
    }
}
