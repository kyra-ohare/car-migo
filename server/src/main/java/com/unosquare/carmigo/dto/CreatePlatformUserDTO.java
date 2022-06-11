package com.unosquare.carmigo.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class CreatePlatformUserDTO {

  private String firstName;

  private String lastName;

  private Instant dob;

  private String email;

  private String password;

  private String phoneNumber;
}
