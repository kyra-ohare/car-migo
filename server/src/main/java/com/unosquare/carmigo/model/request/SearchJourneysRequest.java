package com.unosquare.carmigo.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import lombok.Data;

@Data
public class SearchJourneysRequest {

  @Positive
  private int locationIdFrom;

  @Positive
  private int locationIdTo;

  @NotNull
  private Instant dateTimeFrom;

  @NotNull
  private Instant dateTimeTo;
}
