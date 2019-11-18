package com.lendico.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private Error processFieldErrors(List<FieldError> fieldErrors) {
        Error error = new Error(BAD_REQUEST.value(), "validation error");
        for (FieldError fieldError: fieldErrors) {
            error.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return error;
    }

    static class Error {
        private final int status;
        private final String message;
        private List<FieldCustomError> fieldErrors = new ArrayList<>();

        Error(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public void addFieldError(String path, String message) {
        	FieldCustomError error = new FieldCustomError(path, message);
            fieldErrors.add(error);
        }

        public List<FieldCustomError> getFieldErrors() {
            return fieldErrors;
        }
    }
    
    @JsonAutoDetect(fieldVisibility = Visibility.ANY)
    static class FieldCustomError implements Serializable{
    	
		private static final long serialVersionUID = -4086582400247958139L;
		
		private String defaultMessage;
    	private String field;
    	
    	FieldCustomError(String fieldName, String message) {
            this.setField(fieldName);
            this.setDefaultMessage(message);
        }

		public String getDefaultMessage() {
			return defaultMessage;
		}

		public void setDefaultMessage(String defaultMessage) {
			this.defaultMessage = defaultMessage;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}
    }
}
