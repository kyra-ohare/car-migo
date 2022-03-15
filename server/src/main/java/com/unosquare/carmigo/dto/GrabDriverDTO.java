package com.unosquare.carmigo.dto;

import lombok.Data;

@Data
public class GrabDriverDTO
{
    private int id;

    private String licenseNumber;

    private GrabPlatformUserDTO platformUser;
}
