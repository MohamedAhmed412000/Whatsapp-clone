package com.project.core.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class UserAlreadyHaveRoleException extends ApplicationException {

    public UserAlreadyHaveRoleException() {
        super(CoreErrorsEnum.USER_ALREADY_HAVE_ROLE);
    }

    public UserAlreadyHaveRoleException(String message) {
        super(CoreErrorsEnum.USER_ALREADY_HAVE_ROLE, message);
    }
}
