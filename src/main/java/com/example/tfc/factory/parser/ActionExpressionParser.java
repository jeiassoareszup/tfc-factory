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

    @Override
    String evaluate() {

        if ("CONTEXT".equals(getAction())) {
            String value;
            if("CONTEXT".equals(getParameter())){
                value = "this." + getOutput();
            } else {
                value = getValue();
            }
            this.field = new TypeScriptFieldDTO(getComponentName(), value);
            return TypeScriptTemplateUtils.getAttribution(this.field);
        }

        String variableName = BSCHCrossRelationComponentResolver.getVariableName(getComponentName(), BSCHCrossRelationComponentResolver.getAttributeName(getAction(), getParameter()));
        this.field = new TypeScriptFieldDTO(variableName, getValue());
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
