package io.wodo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
class RestExceptionHandler {

    private Map<String, Object> createErrorMap(ServerHttpRequest req, int value, String ex) {
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("timestap", Instant.now().toString());
        errors.put("path", req.getPath().toString());
        errors.put("status", value);
        errors.put("error", ex);
        errors.put("requestId", req.getId());
        return errors;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity handleMethodArgumentNotValid(WebExchangeBindException ex, ServerHttpRequest req) {

        Map<String, Object> errors = createErrorMap(req, 400, "Validation error");

        List<ValidationError> fieldErrors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(new ValidationError(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
        }
        errors.put("errors", fieldErrors);

        return status(HttpStatus.BAD_REQUEST).body(errors);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity runtimeException(RuntimeException ex, ServerHttpRequest req) {
        Map<String, Object> errors = createErrorMap(req, 500, ex.getMessage());
        return status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }



    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFound(NotFoundException ex, ServerHttpRequest req) {
        Map<String, Object> errors = createErrorMap(req, 404, ex.getMessage());
        return status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(UnexpectedVersionException.class)
    public ResponseEntity unexpectedTransactionVersion(UnexpectedVersionException ex, ServerHttpRequest req) {
        Map<String, Object> errors = createErrorMap(req, 412, ex.getMessage());
        return status(HttpStatus.PRECONDITION_FAILED).body(errors);
    }

    @AllArgsConstructor
    @Data
    public static class ValidationError implements Serializable {

        private String fieldName;
        private Object value;
        private String message;
    }
}