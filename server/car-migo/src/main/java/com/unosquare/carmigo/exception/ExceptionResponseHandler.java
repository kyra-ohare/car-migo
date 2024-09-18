package com.unosquare.carmigo.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import java.util.stream.Collectors;
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
      HttpRequestMethodNotSupportedException.class,
      DataIntegrityViolationException.class})
  public ResponseEntity<ErrorResponse> handleNotFoundException(final Exception exception) {
    return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleConflictException(final Exception exception) {
    return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<ErrorResponse> handleUnprocessableEntityException(final Exception exception) {
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

  @ExceptionHandler({MaxPassengerLimitException.class})
  public ResponseEntity<ErrorResponse> handleNotAcceptableException(final Exception exception) {
    return ExceptionBuilder.buildErrorResponseRepresentation(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
  }

  /**
   * Handles exception thrown by {@link jakarta.validation.Valid} in controllers which parses error messages.
   *
   * @param methodArgumentNotValidException {@link MethodArgumentNotValidException}.
   * @return Response body as {@link ErrorResponse}.
   */
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
