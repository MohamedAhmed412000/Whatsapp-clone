package com.project.whatsapp.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatUserRoleEnum {
    CREATOR(0),
    ADMIN(1),
    MEMBER(2),
    ;

    private final int priority;
}
