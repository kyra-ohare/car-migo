package com.unosquare.carmigo.dto;

import com.unosquare.carmigo.dto.response.UserAccessStatusResponse;
import java.time.Instant;
import lombok.Data;

/**
 * Data Transfer Object used to initialize UserAccessStatusResponse data.<br>
 * If using {@link com.unosquare.carmigo.dto.request.PlatformUserRequest} instead,
 * Hibernate throws a Lazy Initialization Exception.
 */
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

  private UserAccessStatusResponse userAccessStatus;
}
