package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PassengerViewModel
{
    @JsonProperty("id")
    private int id;

    @JsonProperty("user")
    private PlatformUserViewModel user;
}
