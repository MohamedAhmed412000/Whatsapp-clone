package com.project.user.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class ChatNotFoundException extends ApplicationException {

    public ChatNotFoundException() {
        super(UserErrorsEnum.CHAT_NOT_FOUND);
    }

    public ChatNotFoundException(String message) {
        super(UserErrorsEnum.CHAT_NOT_FOUND, message);
    }
}
