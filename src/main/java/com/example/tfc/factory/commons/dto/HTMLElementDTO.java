package com.example.tfc.factory.commons.dto;

import com.example.tfc.factory.commons.enums.HTMLElementType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HTMLElementDTO {

    private HTMLElementType type;
    private List<HTMLElementAttributeDTO> attributes = new ArrayList<>();
    private String text;
    private List<HTMLElementDTO> children = new ArrayList<>();

    public HTMLElementDTO addAttribute(String name, String value) {

        if (value != null) {
            this.attributes.add(new HTMLElementAttributeDTO(name, value));
        }
        return this;
    }
}
