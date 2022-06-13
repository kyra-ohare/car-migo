package com.unosquare.carmigo.model.request;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateCalculateRouteCriteria {

  @NotNull
  private String cityFrom;

  @NotNull
  private String countryFrom;

  @NotNull
  private String cityTo;

  @NotNull
  private String countryTo;
}
