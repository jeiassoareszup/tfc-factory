package com.example.tfc.factory.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoadClassDTO {

    @JsonProperty("class")
    private String clazz;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
