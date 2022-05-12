package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateAuthenticationViewModel
{
    @Size(max = 100)
    @NotNull
    @JsonProperty("email")
    private String email;

    @Size(max = 50)
    @NotNull
    @JsonProperty("password")
    private String password;
}
