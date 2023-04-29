package com.unosquare.carmigo.model.request;

import static com.unosquare.carmigo.constant.AppConstants.EMAIL_MAX_SIZE;
import static com.unosquare.carmigo.constant.AppConstants.EMAIL_MIN_SIZE;
import static com.unosquare.carmigo.constant.AppConstants.PASSWORD_MAX_SIZE;
import static com.unosquare.carmigo.constant.AppConstants.PASSWORD_MIN_SIZE;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAuthenticationViewModel {

  @NotEmpty
  @Size(min = EMAIL_MIN_SIZE, max = EMAIL_MAX_SIZE)
  private String email;

  @NotEmpty
  @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE)
  private String password;
}
