package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class PlatformUserViewModel
{
    @JsonProperty("id")
    private int id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("dob")
    private Instant dob;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("userAccessStatus")
    private UserAccessStatusViewModel userAccessStatus;
}
