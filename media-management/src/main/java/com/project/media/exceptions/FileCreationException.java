package com.project.media.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class FileCreationException extends ApplicationException {

    public FileCreationException() {
        super(MediaErrorsEnum.FILE_CREATION_ERROR);
    }

    public FileCreationException(String message) {
        super(MediaErrorsEnum.FILE_CREATION_ERROR, message);
    }

}
