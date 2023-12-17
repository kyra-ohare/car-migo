package com.unosquare.carmigo.dto.response;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object from the Service back to Controller.
 */
@Getter
@Setter
@NoArgsConstructor
public class PlatformUserResponse {

  private int id;

  private Instant createdDate;

  private String firstName;

  private String lastName;

  private Instant dob;

  private String email;

  private String phoneNumber;

  private boolean isPassenger;

  private boolean isDriver;

  private UserAccessStatusResponse userAccessStatus;
}
