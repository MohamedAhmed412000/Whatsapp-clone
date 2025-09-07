package com.project.user.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class UpdateActionNotAllowedException extends ApplicationException {

    public UpdateActionNotAllowedException() {
        super(UserErrorsEnum.UPDATE_ACTION_NOT_ALLOWED);
    }

    public UpdateActionNotAllowedException(String message) {
        super(UserErrorsEnum.UPDATE_ACTION_NOT_ALLOWED, message);
    }
}
