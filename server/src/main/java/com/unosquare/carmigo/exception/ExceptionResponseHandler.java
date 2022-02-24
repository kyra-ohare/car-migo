package com.unosquare.carmigo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(final Exception exception)
    {
//        exception.getMessage()
        return ExceptionBuilder.buildErrorResponseRepresentation(
                HttpStatus.NO_CONTENT, "Resource not found."
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleBeanValidationException(
            final MethodArgumentNotValidException methodArgumentNotValidException)
    {
        return ExceptionBuilder.buildErrorResponseRepresentation(
                HttpStatus.BAD_REQUEST,
                "Argument not valid: " + methodArgumentNotValidException
                        .getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(objectError -> objectError.getField() + " " + objectError.getDefaultMessage())
                        .collect(Collectors.joining(", "))
        );
    }
}
