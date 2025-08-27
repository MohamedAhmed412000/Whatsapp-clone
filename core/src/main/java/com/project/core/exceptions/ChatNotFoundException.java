package com.project.core.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class ChatNotFoundException extends ApplicationException {

    public ChatNotFoundException() {
        super(CoreErrorsEnum.CHAT_NOT_FOUND);
    }

    public ChatNotFoundException(String message) {
        super(CoreErrorsEnum.CHAT_NOT_FOUND, message);
    }
}
