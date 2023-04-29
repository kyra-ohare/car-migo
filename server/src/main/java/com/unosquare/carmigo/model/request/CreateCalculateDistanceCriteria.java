package com.unosquare.carmigo.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateCalculateDistanceCriteria {

  @NotEmpty
  private String locationFrom;

  @NotEmpty
  private String countryFrom;

  @NotEmpty
  private String locationTo;

  @NotEmpty
  private String countryTo;
}
