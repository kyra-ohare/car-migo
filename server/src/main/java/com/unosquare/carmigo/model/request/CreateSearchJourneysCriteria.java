package com.unosquare.carmigo.model.request;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateSearchJourneysCriteria {

  @Positive
  private int locationIdFrom;

  @Positive
  private int locationIdTo;

  @NotNull
  private Instant dateTimeFrom;

  @NotNull
  private Instant dateTimeTo;
}
