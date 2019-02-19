package com.example.tfc.factory.parser;

import com.example.tfc.factory.commons.dto.TypeScriptComponentDTO;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ConditionExpressionParser extends ExpressionParser {

    private static Map<String, String> operators = new HashMap<>();

    static {
        operators.put("EQ", "===");
        operators.put("NE", "!==");
    }

    public ConditionExpressionParser(String expression) {
        super(expression);
    }

    @Override
    String evaluate() {

        StringBuilder builder = new StringBuilder();

        if ("ISCLICKED".equals(getAction())) {
            return "";
        }

        builder.append("this.");
        builder.append(TypeScriptComponentDTO.parseVariableName(getComponentName()));
        builder.append(" ");
        builder.append(operators.get(getOperator()));
        builder.append(" ");
        builder.append(decorateValue(getParameter()));

        return builder.toString();
    }
}
