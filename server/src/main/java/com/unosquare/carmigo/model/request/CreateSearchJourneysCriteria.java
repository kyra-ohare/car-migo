package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class CreateSearchJourneysCriteria {

  @Range(min = 1)
  @JsonProperty("locationIdFrom")
  final int locationIdFrom;

  @Range(min = 1)
  @JsonProperty("locationIdTo")
  final int locationIdTo;

  @NotNull
  @JsonProperty("dateTimeFrom")
  final Instant dateTimeFrom;

  @NotNull
  @JsonProperty("dateTimeTo")
  final Instant dateTimeTo;
}
