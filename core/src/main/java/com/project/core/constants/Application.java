package com.project.core.constants;

public class Application {
    private Application() {}
    public static final int LAST_ACTIVE_INTERVAL_IN_MINUTES = 5;
    public static final String MSG_MEDIA_PREFIX = "MSG_";
    public static final String CHAT_MEDIA_PREFIX = "CHAT_";
    public static final int PAGE_SIZE = 20;
    public static final String RABBITMQ_NOTIFICATIONS_QUEUE = "chat.notifications";
}
