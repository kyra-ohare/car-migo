package com.unosquare.carmigo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DistanceRequest {

  @NotEmpty
  private String locationFrom;

  @NotEmpty
  private String countryFrom;

  @NotEmpty
  private String locationTo;

  @NotEmpty
  private String countryTo;
}
