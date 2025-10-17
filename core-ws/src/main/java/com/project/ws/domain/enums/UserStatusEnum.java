package com.project.ws.domain.enums;

public enum UserStatusEnum {
    CONNECTED,
    DISCONNECTED,
    ;
    public static UserStatusEnum fromValue(String value) {
        return switch (value) {
            case "CONNECTED" -> CONNECTED;
            case "DISCONNECTED" -> DISCONNECTED;
            default -> null;
        };
    }

    public NotificationTypeEnum getNotificationType() {
        return switch (this) {
            case CONNECTED -> NotificationTypeEnum.USER_CONNECTED;
            case DISCONNECTED -> NotificationTypeEnum.USER_DISCONNECTED;
        };
    }
}
