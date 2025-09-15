package com.project.ws.services;

import com.project.ws.domain.dto.Notification;

public interface NotificationService {
    void handleNotification(Notification notification);
}
