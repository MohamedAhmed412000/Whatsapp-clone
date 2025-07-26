package com.project.whatsapp.services;

import com.project.whatsapp.domain.dto.Notification;

import java.util.List;

public interface NotificationService {
    void sendNotification(List<String> receiverIds, Notification notification);
}
