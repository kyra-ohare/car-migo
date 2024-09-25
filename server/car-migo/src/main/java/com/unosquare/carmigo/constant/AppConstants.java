package com.unosquare.carmigo.constant;

/**
 * Constants used throughout the application.
 */
public interface AppConstants {

  int EMAIL_MIN_SIZE = 5;
  int EMAIL_MAX_SIZE = 100;
  String EMAIL_REGEX = "^([^ @])+@([^ \\.@]+\\.)+([^ \\.@])+$";
  String SPECIAL_CHARACTERS = "@#$%^&+=!?";
  int PASSWORD_MIN_SIZE = 8;
  int PASSWORD_MAX_SIZE = 65;
  String VALID_PASSWORD_MESSAGE = "must be between " + PASSWORD_MIN_SIZE + " and "
      + PASSWORD_MAX_SIZE + " characters and at least 2 of the following: alphanumeric characters and/or one special"
      + "character ( " + SPECIAL_CHARACTERS + " ) and/or one capital letter";

  int ALIAS_CURRENT_USER = 0;

  String NOT_PERMITTED = "Not permitted";
}
