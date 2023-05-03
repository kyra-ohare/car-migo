package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PassengerResponse {

  private int id;

  @JsonProperty("user")
  private PlatformUserResponse platformUser;
}