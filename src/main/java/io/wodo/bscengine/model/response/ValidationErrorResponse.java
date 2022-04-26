package io.wodo.bscengine.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ValidationErrorResponse {

    private String code;
    private String message;
    private Integer statusCode;
    private List<ValidationError> errors;

    public ValidationErrorResponse(Integer statusCode, String message, String code) {
        this.statusCode = statusCode;
        this.message = message;
        this.code = code;
    }

    public void addValidationError(String field, Object value, String message) {
        if (errors == null)
            errors = new ArrayList<>();

        ValidationError validationError = new ValidationError(field, value, message);
        errors.add(validationError);
    }

    @AllArgsConstructor
    @Data
    public static class ValidationError implements Serializable {

        private String fieldName;
        private Object value;
        private String message;
    }

}