package com.example.tfc.factory.commons.dto;

import com.example.tfc.factory.commons.Constants;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TypeScriptComponentDTO {
    private String fileName;
    private List<TypeScriptFunctionDTO> functions = new ArrayList<>();
    private List<TypeScriptFieldDTO> fields = new ArrayList<>();
    private List<TypeScriptImportDTO> imports = new ArrayList<>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<TypeScriptFunctionDTO> getFunctions() {
        return functions;
    }

    public void setFunctions(List<TypeScriptFunctionDTO> functions) {
        this.functions = functions;
    }

    public List<TypeScriptFieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<TypeScriptFieldDTO> fields) {
        this.fields = fields;
    }

    public List<TypeScriptImportDTO> getImports() {
        return imports;
    }

    public void setImports(List<TypeScriptImportDTO> imports) {
        this.imports = imports;
    }

    public void checkDeclaration(String var, String value) {
        if (!StringUtils.isEmpty(var) && this.getFields().stream().noneMatch(f -> f.getName().equals(var))) {
            if (!StringUtils.isEmpty(value) && value.startsWith("!")) {
                value = value.substring(1);
            }
            this.getFields().add(new TypeScriptFieldDTO(parseVariableName(var), value));
        }
    }

    public static String parseVariableName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        String result = name.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS, "");
        return result;
    }
}
