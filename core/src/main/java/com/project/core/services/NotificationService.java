package com.project.core.services;

import com.project.core.domain.dto.Notification;

public interface NotificationService {
    void sendNotificationAsync(Notification notification);
}
