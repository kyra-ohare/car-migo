package com.unosquare.carmigo.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
// import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
// import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handles exceptions triggered throughout the application.
 */
@ControllerAdvice
public class ExceptionResponseHandler {

  @ExceptionHandler({
      EmptyResultDataAccessException.class,
      EntityNotFoundException.class,
      ResourceNotFoundException.class,
      NoResultException.class,
      HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<ErrorResponse> handleNotFoundException(final Exception exception) {
    return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler({
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
      PatchException.class,
      BindException.class})
  public ResponseEntity<ErrorResponse> handleBadRequestException(final Exception exception) {
    return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  //TODO: is this needed? commenting it out to see if his is every called.
  //@ExceptionHandler({MethodArgumentNotValidException.class})
  //public ResponseEntity<ErrorResponse> handleBeanValidationException(
  //    final MethodArgumentNotValidException methodArgumentNotValidException) {
  //  return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.BAD_REQUEST,
  //      "Argument not valid: " + methodArgumentNotValidException.getBindingResult().getFieldErrors()
  //          .stream()
  //          .map(objectError -> objectError.getField() + " " + objectError.getDefaultMessage())
  //          .collect(Collectors.joining(", ")));
  //}
}
