package com.unosquare.carmigo.exception;

import java.util.stream.Collectors;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionResponseHandler {

  @ExceptionHandler({
      EmptyResultDataAccessException.class,
      EntityNotFoundException.class,
      ResourceNotFoundException.class,
      NoResultException.class,
      PatchException.class,
      HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<ErrorResponse> handleNotFoundException(final Exception exception) {
    return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler({
      DataIntegrityViolationException.class,
      EntityExistsException.class,
      IllegalStateException.class})
  public ResponseEntity<ErrorResponse> handleConflictException(final Exception exception) {
    return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.CONFLICT, exception.getMessage());
  }

  @ExceptionHandler({
      UnauthorizedException.class,
      ExpiredJwtException.class,
      BadCredentialsException.class})
  public ResponseEntity<ErrorResponse> handleForbiddenException(final Exception exception) {
    return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.FORBIDDEN, exception.getMessage());
  }

  @ExceptionHandler({
      HttpMessageNotReadableException.class,
      BindException.class})
  public ResponseEntity<ErrorResponse> handleBadRequestException(final Exception exception) {
    return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponse> handleBeanValidationException(
      final MethodArgumentNotValidException methodArgumentNotValidException) {
    return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.BAD_REQUEST,
        "Argument not valid: " + methodArgumentNotValidException.getBindingResult().getFieldErrors()
            .stream()
            .map(objectError -> objectError.getField() + " " + objectError.getDefaultMessage())
            .collect(Collectors.joining(", ")));
  }
}
