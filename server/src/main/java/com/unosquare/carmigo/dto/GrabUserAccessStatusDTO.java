package com.unosquare.carmigo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
//@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrabUserAccessStatusDTO
{
    private int id;

    private String status;
}
