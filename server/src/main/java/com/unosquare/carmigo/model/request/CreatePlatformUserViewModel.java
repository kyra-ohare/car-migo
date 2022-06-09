package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unosquare.carmigo.annotation.ValidPassword;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

import static com.unosquare.carmigo.contant.AppConstants.EMAIL_REGEX;

@Data
public class CreatePlatformUserViewModel
{
    @Size(max = 255)
    @NotEmpty
    @NotNull
    @JsonProperty("firstName")
    private String firstName;

    @Size(max = 255)
    @NotEmpty
    @NotNull
    @JsonProperty("lastName")
    private String lastName;

    @NotNull
    @JsonProperty("dob")
    @Temporal(TemporalType.DATE)
    private Instant dob;

    @Size(max = 100)
    @NotEmpty
    @NotNull
    @JsonProperty("email")
    @Email(regexp = EMAIL_REGEX)
    private String email;

    @Size(max = 50)
    @NotEmpty
    @NotNull
    @JsonProperty("password")
    @ValidPassword
    private String password;

    @Size(max = 50)
    @NotNull
    @JsonProperty("phoneNumber")
    private String phoneNumber;
}
