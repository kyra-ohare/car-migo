package com.unosquare.carmigo.exception;

public class UnauthorizedException extends RuntimeException {

  private static final long serialVersionUID = -1547464948480870090L;

  public UnauthorizedException(final String message) {
    super(message);
  }
}
