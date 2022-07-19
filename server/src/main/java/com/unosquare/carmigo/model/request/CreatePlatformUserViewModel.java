package com.unosquare.carmigo.model.request;

import static com.unosquare.carmigo.constant.AppConstants.EMAIL_REGEX;

import com.fasterxml.jackson.annotation.JsonProperty;
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

  @Size(max = 255)
  @NotEmpty
  @NotNull
  @JsonProperty("firstName")
  private String firstName;

  @Size(max = 255)
  @NotEmpty
  @NotNull
  @JsonProperty("lastName")
  private String lastName;

  @NotNull
  @JsonProperty("dob")
  @Temporal(TemporalType.DATE)
  private Instant dob;

  @Size(max = 100)
  @NotEmpty
  @NotNull
  @JsonProperty("email")
  @Email(regexp = EMAIL_REGEX)
  private String email;

  @Size(max = 50)
  @NotEmpty
  @NotNull
  @JsonProperty("password")
  @ValidPassword
  private String password;

  @Size(max = 50)
  @NotNull
  @JsonProperty("phoneNumber")
  private String phoneNumber;
}
