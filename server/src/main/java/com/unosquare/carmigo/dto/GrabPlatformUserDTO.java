package com.unosquare.carmigo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
//@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrabPlatformUserDTO
{
    private int id;

    private String firstName;

    private String lastName;

    private Instant dob;

    private String email;

    private String password;

    private String phoneNumber;

    private GrabUserAccessStatusDTO userAccessStatus;
}
