package com.example.tfc.factory.parser;

import com.example.tfc.factory.commons.dto.TypeScriptFieldDTO;
import com.example.tfc.factory.resolver.component.BSCHCrossRelationComponentResolver;
import com.example.tfc.factory.utils.TypeScriptTemplateUtils;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ActionExpressionParser extends ExpressionParser {

    private static Map<String, String> behaviorsValues = new HashMap<>();

    static {
        behaviorsValues.put("OPT", "false");
        behaviorsValues.put("RDONLY", "true");
        behaviorsValues.put("EREQ", "true");
        behaviorsValues.put("RDONLYEMPTY", "true");
    }

    public ActionExpressionParser(String expression) {
        super(expression);
    }

    @Override
    String evaluate() {

        if ("CONTEXT".equals(getAction())) {
            String value;
            if("CONTEXT".equals(getParameter())){
                value = "this." + getOutput();
            } else {
                value = getValue();
            }
            return TypeScriptTemplateUtils.getAttribution(new TypeScriptFieldDTO(getComponentName(), value));
        }

        String variableName = BSCHCrossRelationComponentResolver.getVariableName(getComponentName(), BSCHCrossRelationComponentResolver.getAttributeName(getAction(), getParameter())[0]);
        return TypeScriptTemplateUtils.getAttribution(new TypeScriptFieldDTO(variableName, getValue()));
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
