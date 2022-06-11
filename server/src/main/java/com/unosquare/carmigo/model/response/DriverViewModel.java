package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DriverViewModel {

  @JsonProperty("id")
  private int id;

  @JsonProperty("licenseNumber")
  private String licenseNumber;

  @JsonProperty("user")
  private PlatformUserViewModel platformUser;
}
