package com.project.whatsapp.services;

import com.project.whatsapp.domain.dto.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void sendNotification(List<UUID> receiverIds, Notification notification);
}
