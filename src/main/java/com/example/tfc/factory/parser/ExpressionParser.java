package com.example.tfc.factory.parser;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ExpressionParser {

    private static Map<String, String> behaviorsValues = new HashMap<>();

    private String componentName;
    private String action;
    private String parameterType;
    private String operator;
    private String output;
    private String parameter;

    public ExpressionParser(String condition) {
        this.parse(condition);
    }

    private void parse(String expression) {
        String[] members = expression.split(";");

        if (members.length != 6) {
            throw new IllegalArgumentException("ExpressionParser must have 6 members");
        }

        this.componentName = members[0];
        this.action = members[1];
        this.parameterType = members[2];
        this.operator = members[3];
        this.output = members[4];
        this.parameter = members[5];
    }

    abstract String evaluate();

    String decorateValue(String value){

        if ("ALPHANUMERIC".equals(getParameterType()) && "VALUE".equals(getOutput())) {

            if("NULL".equals(value)){
                return value.toLowerCase();
            }

            return "\"" + value + "\"";
        }
        return value;
    }
}