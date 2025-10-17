package com.project.ws.services;

import java.util.Map;

public interface NotificationService {
    void handleUserStatus(Map<String,Object> messageBody);
}
