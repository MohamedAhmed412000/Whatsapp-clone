package com.project.media.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class FileNotFoundException extends ApplicationException {

    public FileNotFoundException() {
        super(MediaErrorsEnum.FILE_NOT_FOUND);
    }

    public FileNotFoundException(String message) {
        super(MediaErrorsEnum.FILE_NOT_FOUND, message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(MediaErrorsEnum.FILE_NOT_FOUND, message);
        this.initCause(cause);
    }
}
