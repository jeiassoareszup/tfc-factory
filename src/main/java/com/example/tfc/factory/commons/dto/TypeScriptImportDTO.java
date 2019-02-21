package com.example.tfc.factory.commons.dto;

public class TypeScriptImportDTO {
    private String[] name;
    private String path;

    public TypeScriptImportDTO(String[] name, String path) {
        this.name = name;
        this.path = path;
    }

    public String[] getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
