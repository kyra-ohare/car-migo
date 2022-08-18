package com.unosquare.carmigo.constant;

public interface AppConstants {

  int EMAIL_MIN_SIZE = 5;
  int EMAIL_MAX_SIZE = 100;
  String EMAIL_REGEX = "^([^ @])+@([^ \\.@]+\\.)+([^ \\.@])+$";
  String SPECIAL_CHARACTERS = "@#$%^&+=!?";
  int PASSWORD_MIN_SIZE= 8;
  int PASSWORD_MAX_SIZE= 65;
  String VALID_PASSWORD_MESSAGE = "Password must contain between 8 and 20 characters and at least 2 of the " +
      "following: Alphanumeric characters, one special character ( " + SPECIAL_CHARACTERS + " ), " +
      "one capital letter.";

  int ALIAS_CURRENT_USER = 0;

  String ACTIVE = "ACTIVE";
  String ADMIN = "ADMIN";
  String DEV = "DEV";
  String LOCKED_OUT = "LOCKED_OUT";
  String STAGED = "STAGED";
  String SUSPENDED = "SUSPENDED";

  String NOT_PERMITTED = "Not permitted.";
}
