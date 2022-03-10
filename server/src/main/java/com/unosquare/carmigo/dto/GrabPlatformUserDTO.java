package com.unosquare.carmigo.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class GrabPlatformUserDTO
{
    private int id;

    private Instant createdDate;

    private String firstName;

    private String lastName;

    private Instant dob;

    private String email;

    private String password;

    private String phoneNumber;

    private GrabUserAccessStatusDTO userAccessStatus;
}
