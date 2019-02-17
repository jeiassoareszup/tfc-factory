package com.example.tfc.factory.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceDescriptionDTO {
    private String name;
    private String method;
    private String url;
}
