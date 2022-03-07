package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreateDriverViewModel
{
    @Size(max = 100)
    @JsonProperty("licenseNumber")
    private String licenseNumber;
}
