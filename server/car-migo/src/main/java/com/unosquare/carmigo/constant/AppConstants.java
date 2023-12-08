package com.unosquare.carmigo.constant;

/**
 * Constants used throughout the application.
 */
public final class AppConstants {

  private AppConstants() {}

  public static final int EMAIL_MIN_SIZE = 5;
  public static final int EMAIL_MAX_SIZE = 100;
  public static final String EMAIL_REGEX = "^([^ @])+@([^ \\.@]+\\.)+([^ \\.@])+$";
  public static final String SPECIAL_CHARACTERS = "@#$%^&+=!?";
  public static final int PASSWORD_MIN_SIZE = 8;
  public static final int PASSWORD_MAX_SIZE = 65;
  public static final String VALID_PASSWORD_MESSAGE = "must be between " + PASSWORD_MIN_SIZE + " and " +
      PASSWORD_MAX_SIZE + " characters and at least 2 of the following: alphanumeric characters and/or one special"
      + "character ( " + SPECIAL_CHARACTERS + " ) and/or one capital letter";

  public static final int ALIAS_CURRENT_USER = 0;

  public static final String NOT_PERMITTED = "Not permitted";
}
