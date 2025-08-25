package com.project.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralCodesEnum implements IContentException {
    SUCCESS(HttpStatus.OK, "I000000", "Request is finished successfully"),
    MISSING_PARAM(HttpStatus.BAD_REQUEST, "E000001", "Missing required parameter"),
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "E000002", "Constraint violation"),
    GENERAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E000003", "General error"),
    ;

    private final HttpStatus httpStatus;
    private final String applicationCode;
    private final String message;

    @Override
    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
