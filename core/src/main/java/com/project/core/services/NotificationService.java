package com.project.core.services;

import com.project.core.domain.dto.Notification;

import java.util.List;

public interface NotificationService {
    void sendNotification(List<String> receiverIds, Notification notification);
}
