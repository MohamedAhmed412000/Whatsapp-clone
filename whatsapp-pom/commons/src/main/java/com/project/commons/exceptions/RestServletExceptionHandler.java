package com.project.commons.exceptions;

import com.project.commons.constants.Headers;
import com.project.commons.enums.GeneralCodesEnum;
import com.project.commons.enums.IContentException;
import com.project.commons.rest.outbound.dto.ErrorBody;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = Type.SERVLET)
public class RestServletExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
        @NonNull TypeMismatchException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        ErrorBody body = ErrorBody.builder()
            .message(ex.getMessage())
            .details(new String[] {String.format("Parameter [%s] has invalid value [%s]. Expected type is [%s]", 
                ex.getPropertyName(), ex.getValue(), ex.getRequiredType() != null ? 
                    ex.getRequiredType() : "unknown"
            )})
            .build();
        log.error(ex.getMessage(), ex);
        return createResponseEntity(body, GeneralCodesEnum.CONSTRAINT_VIOLATION);
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        String[] details = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> String.format(
                "Field [%s] %s", fieldError.getField(), fieldError.getDefaultMessage()
            )).toArray(String[]::new);

        ErrorBody body = ErrorBody.builder()
            .message(ex.getMessage())
            .details(details)
            .build();
        log.error(ex.getMessage(), ex);
        return createResponseEntity(body, GeneralCodesEnum.CONSTRAINT_VIOLATION);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        @NonNull MissingServletRequestParameterException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        ErrorBody body = ErrorBody.builder()
            .message(ex.getMessage())
            .details(new String[]{String.format("The field [%s] is required", ex.getParameterName())})
            .build();
        log.error(ex.getMessage(), ex);
        return createResponseEntity(body, GeneralCodesEnum.MISSING_PARAM);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        @NonNull HttpMessageNotReadableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        ErrorBody body = ErrorBody.builder()
            .message("Malformed JSON request")
            .details(new String[]{ex.getMostSpecificCause().getMessage()})
            .build();
        log.error(ex.getMessage(), ex);
        return createResponseEntity(body, GeneralCodesEnum.MISSING_PARAM);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleApplicationException(ApplicationException ex) {
        ErrorBody body = ErrorBody.builder()
            .message(ex.getMessage())
            .build();
        log.error(ex.getMessage(), ex);
        return createResponseEntity(body, ex.getIContentException());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        ErrorBody body = ErrorBody.builder()
            .message(ex.getMessage())
            .build();
        log.error(ex.getMessage(), ex);
        return createResponseEntity(body, GeneralCodesEnum.GENERAL_ERROR);
    }
    
    protected ResponseEntity<Object> createResponseEntity(
        Object body, @NonNull IContentException iContentException
    ) {
        return ResponseEntity.status(iContentException.getHttpStatusCode())
            .header(Headers.APP_CODE, iContentException.getApplicationCode())
            .body(body);
    }
}
