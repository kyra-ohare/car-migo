package com.unosquare.carmigo.exception;

import java.io.Serial;

/**
 * Triggers when there are issues updating an entity.
 */
public final class PatchException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 5331203260264919110L;

  public PatchException(final String message) {
    super(message);
  }
}
