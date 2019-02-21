package com.example.tfc.factory.commons.dto;

public class TypeScriptFunctionDTO {
    private String access;
    private String name;
    private String[] params;
    private String body;
    private String rtrn;

    public TypeScriptFunctionDTO(String access, String name, String[] params, String body, String rtrn) {
        this.access = access;
        this.name = name;
        this.params = params;
        this.body = body;
        this.rtrn = rtrn;
    }

    public String getAccess() {
        return access;
    }

    public String getName() {
        return name;
    }

    public String[] getParams() {
        return params;
    }

    public String getBody() {
        return body;
    }

    public String getRtrn() {
        return rtrn;
    }
}
