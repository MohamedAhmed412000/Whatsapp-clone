package com.project.user.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException() {
        super(UserErrorsEnum.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(UserErrorsEnum.USER_NOT_FOUND, message);
    }
}
