package com.example.tfc.factory.commons.dto;

import com.example.tfc.factory.commons.enums.HTMLElementType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HTMLElementDTO {

    private HTMLElementType type;
    private String name;
    private List<HTMLElementAttributeDTO> attributes = new ArrayList<>();
    private String text;
    private List<HTMLElementDTO> children = new ArrayList<>();

    public HTMLElementDTO addAttribute(String name, String value) {

        if (value != null && name != null) {

            if (name.equals("name")) {
                this.name = value;
            }

            this.attributes.add(new HTMLElementAttributeDTO(name, value));
        }
        return this;
    }

    public boolean canAddRelationAttribute(String componentName, String attributeName) {
      return this.attributes.stream().anyMatch(a -> a.getName().equals("name") && a.getValue().equals(componentName)) &&
              this.attributes.stream().noneMatch(a -> a.getName().equals(attributeName));
    }
}
