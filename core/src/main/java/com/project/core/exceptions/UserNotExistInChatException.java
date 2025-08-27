package com.project.core.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class UserNotExistInChatException extends ApplicationException {

    public UserNotExistInChatException() {
        super(CoreErrorsEnum.USER_NOT_EXIST_IN_THE_CHAT);
    }

    public UserNotExistInChatException(String message) {
        super(CoreErrorsEnum.USER_NOT_EXIST_IN_THE_CHAT, message);
    }
}
