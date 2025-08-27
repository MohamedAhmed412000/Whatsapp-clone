package com.project.core.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class UpdateActionNotAllowedException extends ApplicationException {

    public UpdateActionNotAllowedException() {
        super(CoreErrorsEnum.UPDATE_ACTION_NOT_ALLOWED);
    }

    public UpdateActionNotAllowedException(String message) {
        super(CoreErrorsEnum.UPDATE_ACTION_NOT_ALLOWED, message);
    }
}
