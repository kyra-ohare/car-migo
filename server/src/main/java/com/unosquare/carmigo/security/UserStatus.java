package com.unosquare.carmigo.security;

/**
 * Represents the current logged-in user status.
 */
public enum UserStatus {
  ACTIVE,
  ADMIN,
  DEV,
  LOCKED_OUT,
  STAGED,
  SUSPENDED
}
