package com.example.tfc.factory.commons.dto;

import com.example.tfc.factory.commons.enums.HTMLElementType;

import java.util.ArrayList;
import java.util.List;

public class HTMLElementDTO {

    private HTMLElementType type;
    private String name;
    private List<HTMLElementAttributeDTO> attributes = new ArrayList<>();
    private String text;
    private List<ServiceCallDTO> calls = new ArrayList<>();
    private List<HTMLElementDTO> children = new ArrayList<>();

    public HTMLElementType getType() {
        return type;
    }

    public void setType(HTMLElementType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HTMLElementAttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<HTMLElementAttributeDTO> attributes) {
        this.attributes = attributes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ServiceCallDTO> getCalls() {
        return calls;
    }

    public void setCalls(List<ServiceCallDTO> calls) {
        this.calls = calls;
    }

    public List<HTMLElementDTO> getChildren() {
        return children;
    }

    public void setChildren(List<HTMLElementDTO> children) {
        this.children = children;
    }

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
