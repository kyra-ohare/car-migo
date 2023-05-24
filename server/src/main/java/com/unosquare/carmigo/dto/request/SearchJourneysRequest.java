package com.unosquare.carmigo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import lombok.Data;

/**
 * Data Transfer Object received by the Controller to the Service.
 * Represents the search criteria to query journeys.
 */
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
