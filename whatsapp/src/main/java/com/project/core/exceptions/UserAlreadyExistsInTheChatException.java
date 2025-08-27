package com.project.core.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class UserAlreadyExistsInTheChatException extends ApplicationException {

    public UserAlreadyExistsInTheChatException() {
        super(CoreErrorsEnum.USER_ALREADY_EXISTS_IN_THE_CHAT);
    }

    public UserAlreadyExistsInTheChatException(String message) {
        super(CoreErrorsEnum.USER_ALREADY_EXISTS_IN_THE_CHAT, message);
    }
}
