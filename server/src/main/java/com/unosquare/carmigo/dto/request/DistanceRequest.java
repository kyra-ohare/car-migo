package com.unosquare.carmigo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Data Transfer Object received by the Controller to the Service.
 * Represents the search criteria to query distance.
 */
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
