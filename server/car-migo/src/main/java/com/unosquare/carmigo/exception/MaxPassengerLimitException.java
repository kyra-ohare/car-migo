package com.unosquare.carmigo.exception;

import java.io.Serial;

/**
 * Triggers when passengers try to book a journey when it is full.
 */
public class MaxPassengerLimitException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -2865165685133239163L;

  public MaxPassengerLimitException(String message) {
    super(message);
  }
}
