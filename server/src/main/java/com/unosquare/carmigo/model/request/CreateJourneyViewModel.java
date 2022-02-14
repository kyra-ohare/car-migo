package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateJourneyViewModel {

    @NonNull
    @JsonProperty("locationIdFrom")
    private String locationIdFrom;


    @NonNull
    @JsonProperty("locationIdTo")
    private String locationIdTo;

    @NonNull
    @JsonProperty("maxPassengers")
    private String maxPassengers;

    @NonNull
    @JsonProperty("dateTime")
    private String dateTime;

    @NonNull
    @JsonProperty("driverId")
    private String driverId;
}
