package com.example.tfc.factory.commons.dto;

import java.util.Objects;

public class    TypeScriptFieldDTO {
    private String name;
    private String value;

    public TypeScriptFieldDTO(String name, String value) {
        this.name = name;

        if ("FALSE".equals(value) || "TRUE".equals(value) || "!FALSE".equals(value) || "!TRUE".equals(value)) {
            value = value.toLowerCase();
        }

        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeScriptFieldDTO that = (TypeScriptFieldDTO) o;
        return getName().equals(that.getName()) &&
                getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getValue());
    }
}
