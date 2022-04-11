package com.unosquare.carmigo.dto;

import com.unosquare.carmigo.entity.PlatformUser;
import lombok.Data;

@Data
public class GrabPassengerDTO
{
    private int id;

    private GrabPlatformUserDTO platformUser;
}
