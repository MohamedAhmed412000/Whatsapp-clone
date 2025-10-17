package com.project.ws.controllers;

import com.project.ws.domain.dto.Notification;
import com.project.ws.domain.enums.UserStatusEnum;
import com.project.ws.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserStatusController {
    private final NotificationService notificationService;

    @MessageMapping("/user-status")
    public void handleUserStatus(Map<String,Object> messageBody) {
        System.out.println("Websocket received: " + messageBody);
        notificationService.handleUserStatus(messageBody);
    }
}
