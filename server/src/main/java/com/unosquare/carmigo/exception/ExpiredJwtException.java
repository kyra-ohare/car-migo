package com.unosquare.carmigo.exception;

public class ExpiredJwtException extends RuntimeException {

  private static final long serialVersionUID = -5632730183246481632L;

  public ExpiredJwtException(final String message) {
    super(message);
  }
}
