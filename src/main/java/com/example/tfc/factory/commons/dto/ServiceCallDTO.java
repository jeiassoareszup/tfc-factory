package com.example.tfc.factory.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceCallDTO {
    private String name;
    private String[] args;
    private String rtrn;
}
