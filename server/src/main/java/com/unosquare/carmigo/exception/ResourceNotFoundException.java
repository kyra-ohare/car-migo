package com.unosquare.carmigo.exception;

import java.io.Serial;

public final class ResourceNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -1151239515208790648L;

  public ResourceNotFoundException(final String message) {
    super(message);
  }
}
