package com.unosquare.carmigo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DriverResponse {

  private int id;

  private String licenseNumber;

//  TODO
//  @JsonProperty("user")
  private PlatformUserResponse platformUser;
}
