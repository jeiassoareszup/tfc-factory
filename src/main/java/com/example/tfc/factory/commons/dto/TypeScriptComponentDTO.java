package com.example.tfc.factory.commons.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TypeScriptComponentDTO {
    private String fileName;
    private List<TypeScriptFunctionDTO> functions = new ArrayList<>();
    private List<TypeScriptFieldDTO> fields = new ArrayList<>();
    private List<TypeScriptImportDTO> imports = new ArrayList<>();

    public void checkDeclaration(String var, String value) {
        if (this.getFields().stream().noneMatch(f -> f.getName().equals(var))) {
            this.getFields().add(new TypeScriptFieldDTO(var, value));
        }
    }
}
