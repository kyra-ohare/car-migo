package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAuthenticationViewModel {

  @Size(max = 100)
  @NotNull
  @JsonProperty("email")
  private String email;

  @Size(max = 50)
  @NotNull
  @JsonProperty("password")
  private String password;
}
