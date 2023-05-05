package com.unosquare.carmigo.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class PlatformUserDto {

  private int id;

  private Instant createdDate;

  private String firstName;

  private String lastName;

  private Instant dob;

  private String email;

  private String password;

  private String phoneNumber;

  private GrabUserAccessStatusDTO userAccessStatus;
}
