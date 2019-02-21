package com.example.tfc.factory.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoadClassDTO {

    @JsonProperty("class")
    private String clazz;
}
