package com.project.whatsapp.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatUserOperationEnum {
    ADD_NEW_USER,
    REMOVE_EXISTING_USER,
    MODIFY_EXISTING_USER_ROLE
}
