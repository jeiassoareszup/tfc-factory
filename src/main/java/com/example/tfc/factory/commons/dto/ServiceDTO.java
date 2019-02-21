package com.example.tfc.factory.commons.dto;

import java.util.ArrayList;
import java.util.List;

public class ServiceDTO {
    private String fileName;
    private List<ServiceDescriptionDTO> descriptions = new ArrayList<>();
    private List<ServiceCallDTO> calls = new ArrayList<>();
    private List<TypeScriptImportDTO> imports = new ArrayList<>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ServiceDescriptionDTO> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ServiceDescriptionDTO> descriptions) {
        this.descriptions = descriptions;
    }

    public List<ServiceCallDTO> getCalls() {
        return calls;
    }

    public void setCalls(List<ServiceCallDTO> calls) {
        this.calls = calls;
    }

    public List<TypeScriptImportDTO> getImports() {
        return imports;
    }

    public void setImports(List<TypeScriptImportDTO> imports) {
        this.imports = imports;
    }
}
