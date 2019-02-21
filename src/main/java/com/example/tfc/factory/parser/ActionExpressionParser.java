package com.example.tfc.factory.parser;

import com.example.tfc.factory.commons.dto.TypeScriptFieldDTO;
import com.example.tfc.factory.resolver.component.BSCHCrossRelationComponentResolver;
import com.example.tfc.factory.utils.TypeScriptTemplateUtils;

import java.util.HashMap;
import java.util.Map;

public class ActionExpressionParser extends ExpressionParser {

    private static Map<String, String> behaviorsValues = new HashMap<>();
    private TypeScriptFieldDTO field;

    static {
        behaviorsValues.put("OPT", "false");
        behaviorsValues.put("RDONLY", "true");
        behaviorsValues.put("EREQ", "true");
        behaviorsValues.put("RDONLYEMPTY", "true");
    }

    public ActionExpressionParser(String expression) {
        super(expression);
    }

    public static Map<String, String> getBehaviorsValues() {
        return behaviorsValues;
    }

    public TypeScriptFieldDTO getField() {
        return field;
    }

    @Override
    String evaluate() {

        if ("CONTEXT".equals(getAction())) {
            String value;
            if ("CONTEXT".equals(getParameter())) {
                value = "this." + getOutput();
            } else {
                value = getValue();
            }
            this.field = new TypeScriptFieldDTO(getComponentName(), value);
            return TypeScriptTemplateUtils.getAttribution(this.field);
        }

        String value;

        if ("VISIBLE".equals(getAction())) {
            value = "!" + getValue();
        } else {
            value = getValue();
        }

        String variableName = BSCHCrossRelationComponentResolver.getVariableName(getComponentName(), BSCHCrossRelationComponentResolver.getAttributeName(getAction(), getParameter()));
        this.field = new TypeScriptFieldDTO(variableName, value);
        return TypeScriptTemplateUtils.getAttribution(this.field);
    }

    public String getValue() {

        if ("BEHAVIOUR".equals(getAction())) {
            return behaviorsValues.get(getParameter());
        }

        if ("VISIBLE".equals(getAction())) {
            return getParameter();
        }

        return decorateValue(getParameter());
    }
}
