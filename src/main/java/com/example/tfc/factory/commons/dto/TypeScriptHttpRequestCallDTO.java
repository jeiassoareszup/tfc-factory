package com.example.tfc.factory.commons.dto;

import java.util.Objects;

public class TypeScriptHttpRequestCallDTO {

    private String functionName;
    private String path;
    private String method;

    public TypeScriptHttpRequestCallDTO(String functionName, String path, String method) {
        this.functionName = functionName;
        this.path = path;
        this.method = method;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public String getTemplate() {
        return this.getMethod().equals("POST") ? "http-post-call" : "http-get-call";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeScriptHttpRequestCallDTO that = (TypeScriptHttpRequestCallDTO) o;
        return Objects.equals(getFunctionName(), that.getFunctionName()) &&
                Objects.equals(getPath(), that.getPath()) &&
                Objects.equals(getMethod(), that.getMethod());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getFunctionName(), getPath(), getMethod());
    }
}
