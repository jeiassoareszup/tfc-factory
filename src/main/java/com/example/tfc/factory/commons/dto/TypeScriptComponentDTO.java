package com.example.tfc.factory.commons.dto;

import com.example.tfc.factory.commons.Constants;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class TypeScriptComponentDTO {
    private String fileName;
    private List<TypeScriptFunctionDTO> functions = new ArrayList<>();
    private List<TypeScriptFieldDTO> fields = new ArrayList<>();
    private List<TypeScriptImportDTO> imports = new ArrayList<>();

    public void checkDeclaration(String var, String value) {
        if (!StringUtils.isEmpty(var) && this.getFields().stream().noneMatch(f -> f.getName().equals(var))) {
            if(value.startsWith("!")){
                value = value.substring(1);
            }
            this.getFields().add(new TypeScriptFieldDTO(parseVariableName(var), value));
        }
    }

    public static String parseVariableName(String name){
        if(StringUtils.isEmpty(name)){
            return null;
        }

        return name.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS, "");
    }
}
