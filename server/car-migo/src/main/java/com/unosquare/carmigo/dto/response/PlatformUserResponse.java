package com.unosquare.carmigo.dto.response;

import java.io.Serial;
import java.io.Serializable;
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
public class PlatformUserResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1259069678904869987L;

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
