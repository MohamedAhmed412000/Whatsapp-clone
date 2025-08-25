package com.project.commons.exceptions;

import com.project.commons.enums.IContentException;
import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException {
    private final IContentException iContentException;

    protected ApplicationException(IContentException iContentException) {
        super(iContentException.getMessage());
        this.iContentException = iContentException;
    }

    protected ApplicationException(IContentException iContentException, String message) {
        super(message);
        this.iContentException = iContentException;
    }
}
