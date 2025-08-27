package com.project.gateway.exceptions;

import com.project.commons.enums.IContentException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GatewayErrorsEnum implements IContentException {
    TOKEN_EXTRACTION_ERROR(HttpStatus.BAD_REQUEST, "E000001", "Can't extract element from token"),
    ;

    private final HttpStatus httpStatus;
    private final String applicationCode;
    private final String message;

    @Override
    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
