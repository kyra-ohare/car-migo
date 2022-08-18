package com.unosquare.carmigo.model.request;

import static com.unosquare.carmigo.constant.AppConstants.EMAIL_MAX_SIZE;
import static com.unosquare.carmigo.constant.AppConstants.EMAIL_MIN_SIZE;
import static com.unosquare.carmigo.constant.AppConstants.EMAIL_REGEX;
import static com.unosquare.carmigo.constant.AppConstants.PASSWORD_MAX_SIZE;
import static com.unosquare.carmigo.constant.AppConstants.PASSWORD_MIN_SIZE;

import com.unosquare.carmigo.annotation.ValidPassword;
import java.time.Instant;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePlatformUserViewModel {

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
