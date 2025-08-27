package com.project.core.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException() {
        super(CoreErrorsEnum.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(CoreErrorsEnum.USER_NOT_FOUND, message);
    }
}
