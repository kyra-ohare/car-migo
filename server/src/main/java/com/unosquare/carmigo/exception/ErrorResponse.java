package com.unosquare.carmigo.exception;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * Holds error response fields.
 */
@Getter
@Builder
public class ErrorResponse {

  private int status;
  private String error;
  private String message;
  private String path;
  private LocalDateTime timestamp;
}
