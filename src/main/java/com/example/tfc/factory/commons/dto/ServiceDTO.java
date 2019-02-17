package com.example.tfc.factory.commons.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ServiceDTO {
    private String fileName;
    List<ServiceDescriptionDTO> descriptions = new ArrayList<>();
    private List<ServiceCallDTO> calls = new ArrayList<>();
    List<TypeScriptImportDTO> imports = new ArrayList<>();
}
