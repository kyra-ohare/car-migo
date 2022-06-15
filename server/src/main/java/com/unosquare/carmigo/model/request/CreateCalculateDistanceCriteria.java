package com.unosquare.carmigo.model.request;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCalculateDistanceCriteria {

  @NotNull
  private String locationFrom;

  @NotNull
  private String countryFrom;

  @NotNull
  private String locationTo;

  @NotNull
  private String countryTo;
}
