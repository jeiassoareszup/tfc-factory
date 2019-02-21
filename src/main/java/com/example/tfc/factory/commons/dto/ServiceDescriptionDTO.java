package com.example.tfc.factory.commons.dto;

public class ServiceDescriptionDTO {
    private String name;
    private String method;
    private String url;

    public ServiceDescriptionDTO(String name, String method, String url) {
        this.name = name;
        this.method = method;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
