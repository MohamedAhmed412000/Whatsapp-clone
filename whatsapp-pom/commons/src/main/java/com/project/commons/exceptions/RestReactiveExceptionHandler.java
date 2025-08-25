package com.project.commons.exceptions;

import com.project.commons.constants.Headers;
import com.project.commons.enums.GeneralCodesEnum;
import com.project.commons.enums.IContentException;
import com.project.commons.rest.outbound.dto.ErrorBody;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = Type.REACTIVE)
public class RestReactiveExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @NonNull Mono<ResponseEntity<Object>> handleMethodValidationException(
        @NonNull MethodValidationException ex,
        @NonNull HttpStatus status,
        @NonNull ServerWebExchange exchange
    ) {
        ErrorBody body = ErrorBody.builder()
            .message(ex.getMessage())
            .details(ex.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(error -> String.format("Field [%s] %s",
                    error.getCodes() != null && error.getCodes().length > 0 ? error.getCodes()[0] : "unknown",
                    error.getDefaultMessage())).toArray(String[]::new))
            .build();
        log.error(ex.getMessage(), ex);
        return createResponseEntity(body, GeneralCodesEnum.CONSTRAINT_VIOLATION);
    }

    @Override
    protected @NonNull Mono<ResponseEntity<Object>> handleMissingRequestValueException(
        @NonNull MissingRequestValueException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull ServerWebExchange exchange
    ) {
        ErrorBody body = ErrorBody.builder()
            .message(ex.getMessage())
            .details(new String[]{String.format("The field [%s] is required", ex.getName())})
            .build();
        log.error(ex.getMessage(), ex);
        return createResponseEntity(body, GeneralCodesEnum.MISSING_PARAM);
    }

    @ExceptionHandler(ApplicationException.class)
    public Mono<ResponseEntity<Object>> handleApplicationException(ApplicationException ex) {
        ErrorBody body = ErrorBody.builder()
            .message(ex.getLocalizedMessage())
            .build();
        log.error(ex.getMessage(), ex);
        return createResponseEntity(body, ex.getIContentException());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Object>> handleGeneralException(Exception ex) {
        ErrorBody body = ErrorBody.builder()
            .message(ex.getLocalizedMessage())
            .build();
        log.error(ex.getMessage(), ex);
        return createResponseEntity(body, GeneralCodesEnum.GENERAL_ERROR);
    }

    protected Mono<ResponseEntity<Object>> createResponseEntity(
        Object body, @NonNull IContentException iContentException
    ) {
        return Mono.just(
            ResponseEntity.status(iContentException.getHttpStatusCode())
                .header(Headers.APP_CODE, iContentException.getApplicationCode())
                .body(body)
        );
    }
}
