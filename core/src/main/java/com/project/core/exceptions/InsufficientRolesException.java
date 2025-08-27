package com.project.core.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class InsufficientRolesException extends ApplicationException {

    public InsufficientRolesException() {
        super(CoreErrorsEnum.INSUFFICIENT_ROLES_TO_TAKE_ACTION);
    }

    public InsufficientRolesException(String message) {
        super(CoreErrorsEnum.INSUFFICIENT_ROLES_TO_TAKE_ACTION, message);
    }
}
