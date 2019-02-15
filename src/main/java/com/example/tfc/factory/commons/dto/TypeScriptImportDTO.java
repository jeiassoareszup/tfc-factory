package com.example.tfc.factory.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TypeScriptImportDTO {
    private String[] name;
    private String path;
}
