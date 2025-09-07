package com.project.user.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class ContactAlreadyExistsException extends ApplicationException {

    public ContactAlreadyExistsException() {
        super(UserErrorsEnum.CONTACT_ALREADY_EXISTS);
    }

    public ContactAlreadyExistsException(String message) {
        super(UserErrorsEnum.CONTACT_ALREADY_EXISTS, message);
    }
}
