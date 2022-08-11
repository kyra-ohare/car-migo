package com.unosquare.carmigo.model.request;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateJourneyViewModel {

  @Positive
  private int locationIdFrom;

  @Positive
  private int locationIdTo;

  @Positive
  private int maxPassengers;

  @NotNull
  private Instant dateTime;
}
