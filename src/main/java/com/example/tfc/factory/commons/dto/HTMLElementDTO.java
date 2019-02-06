package com.example.tfc.factory.commons.dto;

import com.example.tfc.factory.commons.enums.HTMLElementType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HTMLElementDTO {

    private HTMLElementType type;
    private List<String> attributes = new ArrayList<>();
    private String content;
    private List<HTMLElementDTO> elements = new ArrayList<>();

    public HTMLElementDTO addAttribute(String name, String value) {
         this.attributes.add(name + "=\"" + value + "\"");
         return this;
    }
}
