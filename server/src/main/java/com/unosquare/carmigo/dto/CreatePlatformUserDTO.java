package com.unosquare.carmigo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreatePlatformUserDTO
{
    private int id;

    private String firstName;

    private String lastName;

    private LocalDateTime dob;

    private String email;

    private String password;

    private String phoneNumber;
}
