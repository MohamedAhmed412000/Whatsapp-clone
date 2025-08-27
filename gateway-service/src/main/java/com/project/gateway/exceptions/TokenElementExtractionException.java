package com.project.gateway.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class TokenElementExtractionException extends ApplicationException {

    public TokenElementExtractionException() {
        super(GatewayErrorsEnum.TOKEN_EXTRACTION_ERROR);
    }

    public TokenElementExtractionException(String message) {
        super(GatewayErrorsEnum.TOKEN_EXTRACTION_ERROR, message);
    }

}
