package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class CreateSearchJourneysCriteria
{
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
