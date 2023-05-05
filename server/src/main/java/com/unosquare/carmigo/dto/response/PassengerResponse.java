package com.unosquare.carmigo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PassengerResponse {

  private int id;

//  TODO
//  @JsonProperty("user")
  private PlatformUserResponse platformUser;
}
