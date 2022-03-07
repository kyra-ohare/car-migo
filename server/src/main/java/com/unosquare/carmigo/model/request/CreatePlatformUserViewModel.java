package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unosquare.carmigo.annotation.ValidPassword;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
public class CreatePlatformUserViewModel
{
    @Size(max = 255)
    @NotBlank
    @JsonProperty("firstName")
    private String firstName;

    @Size(max = 255)
    @NotBlank
    @JsonProperty("lastName")
    private String lastName;

    @NotBlank
    @JsonProperty("dob")
    @Temporal(TemporalType.DATE)
    private Instant dob;

    @Size(max = 100)
    @NotBlank
    @JsonProperty("email")
    private String email;

    @Size(max = 50)
    @NotBlank
    @JsonProperty("password")
    @ValidPassword
    private String password;

    @Size(max = 50)
    @NotBlank
    @JsonProperty("phoneNumber")
    private String phoneNumber;
}
