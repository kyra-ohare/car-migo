package com.unosquare.carmigo.dto;

import com.unosquare.carmigo.entity.PlatformUser;
import lombok.Data;

@Data
public class GrabDriverDTO
{
    private int id;

    private String licenseNumber;

    private PlatformUser platformUser;
}
