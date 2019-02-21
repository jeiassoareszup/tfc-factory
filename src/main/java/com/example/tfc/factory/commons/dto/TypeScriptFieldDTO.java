package com.example.tfc.factory.commons.dto;

public class TypeScriptFieldDTO {
    private String name;
    private String value;

    public TypeScriptFieldDTO(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
