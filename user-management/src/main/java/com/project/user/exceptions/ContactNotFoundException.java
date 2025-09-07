package com.project.user.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class ContactNotFoundException extends ApplicationException {

    public ContactNotFoundException() {
        super(UserErrorsEnum.CONTACT_NOT_FOUND);
    }

    public ContactNotFoundException(String message) {
        super(UserErrorsEnum.CONTACT_NOT_FOUND, message);
    }
}
