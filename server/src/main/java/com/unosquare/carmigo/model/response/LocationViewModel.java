package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationViewModel
{
    @JsonProperty("id")
    private int id;

    @JsonProperty("description")
    private String description;
}
