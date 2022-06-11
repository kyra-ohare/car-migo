package com.unosquare.carmigo.exception;

public final class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -1151239515208790648L;

  public ResourceNotFoundException(final String message) {
    super(message);
  }
}
