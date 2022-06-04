package com.unosquare.carmigo.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionResponseHandler
{

    @ExceptionHandler({
            EmptyResultDataAccessException.class,
            EntityNotFoundException.class,
            ResourceNotFoundException.class,
            NoResultException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(final Exception exception)
    {
        return ExceptionBuilder.buildErrorResponseRepresentation(
                HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(final Exception exception)
    {
        return ExceptionBuilder.buildErrorResponseRepresentation(
                HttpStatus.CONFLICT, "Duplicate Entity: " + exception.getMessage());
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(final Exception exception)
    {
        return ExceptionBuilder.buildErrorResponseRepresentation(
                HttpStatus.FORBIDDEN, exception.getMessage());
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
