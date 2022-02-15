package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DriverViewModel
{
    @JsonProperty("id")
    private int id;

    @JsonProperty("licenseNumber")
    private String licenseNumber;

    @JsonProperty("user")
    private PlatformUserViewModel user;
}
