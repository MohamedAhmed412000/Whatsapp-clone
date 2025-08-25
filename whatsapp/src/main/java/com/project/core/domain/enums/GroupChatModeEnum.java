package com.project.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupChatModeEnum {
    NORMAL(0, "Any user can add new users or post a message"),
    USERS_MODIFICATION_RESTRICTED(1, "Admins only can add or remove new users"),
    MESSAGES_CREATION_RESTRICTED(2, "Admins only can create messages"),
    ADMIN_RESTRICTED(3, "Admins only can control users or create messages")
    ;

    private final int value;
    private final String description;

    public static GroupChatModeEnum getEnum(int value) {
        for (GroupChatModeEnum mode : GroupChatModeEnum.values()) {
            if (mode.getValue() == value) return mode;
        }
        return NORMAL;
    }
}
