package com.project.user.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class DeleteActionNotAllowedException extends ApplicationException {

    public DeleteActionNotAllowedException() {
        super(UserErrorsEnum.UPDATE_ACTION_NOT_ALLOWED);
    }

    public DeleteActionNotAllowedException(String message) {
        super(UserErrorsEnum.UPDATE_ACTION_NOT_ALLOWED, message);
    }
}
