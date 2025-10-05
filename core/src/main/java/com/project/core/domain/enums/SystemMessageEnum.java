package com.project.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SystemMessageEnum {
    CHAT_CREATED("Chat created"),
    ;

    private final String content;
}
