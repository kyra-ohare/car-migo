package com.unosquare.carmigo.model.request;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class CreateSearchJourneysCriteria {

  @Range(min = 1)
  private int locationIdFrom;

  @Range(min = 1)
  private int locationIdTo;

  @NotNull
  private Instant dateTimeFrom;

  @NotNull
  private Instant dateTimeTo;
}
