package com.example.tfc.factory.commons.dto;

import org.jdom2.Attribute;
import org.jdom2.AttributeType;
import org.jdom2.Namespace;

public class HTMLElementAttributeDTO extends Attribute {

    public HTMLElementAttributeDTO(String name, String value) {
        setName(name);
        super.setValue(value);
        super.setAttributeType(AttributeType.CDATA);
        super.setNamespace(Namespace.NO_NAMESPACE);
    }

    @Override
    public Attribute setName(final String name) {
        super.name = name;
        return this;
    }
}
