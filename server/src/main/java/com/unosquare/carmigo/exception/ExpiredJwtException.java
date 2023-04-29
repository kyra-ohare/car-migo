package com.unosquare.carmigo.exception;

import java.io.Serial;

public class ExpiredJwtException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -5632730183246481632L;

  public ExpiredJwtException(final String message) {
    super(message);
  }
}
