package com.unosquare.carmigo.model.request;

import static com.unosquare.carmigo.constant.AppConstants.EMAIL_MAX_SIZE;
import static com.unosquare.carmigo.constant.AppConstants.EMAIL_MIN_SIZE;
import static com.unosquare.carmigo.constant.AppConstants.EMAIL_REGEX;
import static com.unosquare.carmigo.constant.AppConstants.PASSWORD_MAX_SIZE;
import static com.unosquare.carmigo.constant.AppConstants.PASSWORD_MIN_SIZE;

import com.unosquare.carmigo.annotation.ValidPassword;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.Data;

@Data
public class PlatformUserRequest {

  @NotEmpty
  @Size(min = 1, max = 255)
  private String firstName;

  @NotEmpty
  @Size(min = 1, max = 255)
  private String lastName;

  @NotNull
  @Temporal(TemporalType.DATE)
  private Instant dob;

  @NotEmpty
  @Size(min = EMAIL_MIN_SIZE, max = EMAIL_MAX_SIZE)
  @Email(regexp = EMAIL_REGEX)
  private String email;

  @NotEmpty
  @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE)
  @ValidPassword
  private String password;

  @NotEmpty
  @Size(max = 50)
  private String phoneNumber;
}
