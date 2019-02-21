package com.example.tfc.factory.commons.dto;

public class ServiceCallDTO {
    private String name;
    private String[] args;
    private String rtrn;

    public ServiceCallDTO(String name, String[] args, String rtrn) {
        this.name = name;
        this.args = args;
        this.rtrn = rtrn;
    }

    public String getName() {
        return name;
    }

    public String[] getArgs() {
        return args;
    }

    public String getRtrn() {
        return rtrn;
    }
}
