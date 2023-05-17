package com.unosquare.carmigo.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Builder an error exception.
 */
public class ExceptionBuilder {

  /**
   * Builds an error response.
   * @param httpStatus the error {@link HttpStatus}.
   * @param message what happened.
   * @return a {@link ResponseEntity}.
   */
  public static ResponseEntity<ErrorResponse> buildErrorResponseRepresentation(
      final HttpStatus httpStatus, final String message) {
    return ResponseEntity.status(httpStatus)
        .body(ErrorResponse.builder()
            .status(httpStatus.value())
            .path("Not specified")
            .error(httpStatus.getReasonPhrase())
            .message(message)
            .timestamp(LocalDateTime.now())
            .build());
  }
}
