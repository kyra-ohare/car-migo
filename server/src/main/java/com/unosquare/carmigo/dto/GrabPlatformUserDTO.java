package com.unosquare.carmigo.dto;

import lombok.Data;

import java.time.Instant;

@Data
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
