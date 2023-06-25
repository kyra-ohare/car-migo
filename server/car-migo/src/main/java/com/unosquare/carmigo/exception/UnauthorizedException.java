package com.unosquare.carmigo.exception;

import java.io.Serial;

/**
 * Triggers when a user does not have permissions to perform an action.
 */
public class UnauthorizedException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -1547464948480870090L;

  public UnauthorizedException(final String message) {
    super(message);
  }
}
