package com.project.core.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class DeleteActionNotAllowedException extends ApplicationException {

    public DeleteActionNotAllowedException() {
        super(CoreErrorsEnum.DELETE_ACTION_NOT_ALLOWED);
    }

    public DeleteActionNotAllowedException(String message) {
        super(CoreErrorsEnum.DELETE_ACTION_NOT_ALLOWED, message);
    }
}
