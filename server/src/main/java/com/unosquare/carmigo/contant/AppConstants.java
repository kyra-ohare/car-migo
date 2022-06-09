package com.unosquare.carmigo.contant;

public interface AppConstants
{
    String EMAIL_REGEX = "^([^ @])+@([^ \\.@]+\\.)+([^ \\.@])+$";
    String SPECIAL_CHARACTERS = "@#$%^&+=!?";
    String VALID_PASSWORD_MESSAGE = "Password must contain between 8 and 20 characters and at least 2 of the " +
            "following: Alphanumeric characters, one special character ( " + SPECIAL_CHARACTERS + " ), " +
            "one capital letter.";

    String ACTIVE = "ACTIVE";
    String ADMIN = "ADMIN";
    String DEV = "DEV";
    String LOCKED_OUT = "LOCKED_OUT";
    String STAGED = "STAGED";
    String SUSPENDED = "SUSPENDED";

    String NO_PERMISSIONS = "Not permitted";
}
