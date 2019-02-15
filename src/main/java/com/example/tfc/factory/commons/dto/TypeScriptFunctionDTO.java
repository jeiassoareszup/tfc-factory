package com.example.tfc.factory.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TypeScriptFunctionDTO {
    private String name;
    private String[] params;
    private String body;
}
