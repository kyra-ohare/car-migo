package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class CreateSearchJourneysCriteria
{
    @NotNull
    @JsonProperty("locationIdFrom")
    final int locationIdFrom;

    @NotNull
    @JsonProperty("locationIdTo")
    final int locationIdTo;

    @NotNull
    @JsonProperty("dateTimeFrom")
    final Instant dateTimeFrom;

    @NotNull
    @JsonProperty("dateTimeTo")
    final Instant dateTimeTo;
}
