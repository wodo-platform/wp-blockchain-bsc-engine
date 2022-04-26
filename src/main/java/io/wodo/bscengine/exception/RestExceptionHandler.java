package io.wodo.bscengine.exception;

import io.wodo.bscengine.model.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFound(NotFoundException ex, WebRequest req) {

        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("code", "not_found");
        errors.put("statusCode", "404");
        return status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(),
                                                                            "Validation error. Check 'errors' field for details.", "validation_error");

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        }

        return errorResponse;
    }
}