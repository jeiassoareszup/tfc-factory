package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.parser.RelationParser;
import com.example.tfc.factory.utils.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BSCHCrossRelationComponentResolver extends ComponentResolver {

    private static Map<String, String> expressions = new HashMap<>();

    static {
        expressions.put("BEHAVIOUR RDONLY", Constants.DISABLED_ATTRIBUTE_NAME);
        expressions.put("BEHAVIOUR IREQ", Constants.REQUIRED_ATTRIBUTE_NAME);
        expressions.put("BEHAVIOUR OPT", Constants.DISABLED_ATTRIBUTE_NAME);
        expressions.put("BEHAVIOUR RDONLYEMPTY", Constants.DISABLED_ATTRIBUTE_NAME);
        expressions.put("VISIBLE FALSE", Constants.HIDDEN_ATTRIBUTE_NAME);
        expressions.put("VISIBLE TRUE", Constants.HIDDEN_ATTRIBUTE_NAME);
    }

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

        List actions = ReflectionUtils.getListField(component, "getAction");
        List elseActions = ReflectionUtils.getListField(component, "getElseAction");

        buildHTML(panelDTO, actions, elseActions);
        buildTS(panelDTO, component, actions, elseActions);

        return panelDTO;
    }

    private void buildTS(PanelDTO panelDTO, Object component, List actions, List elseActions) {
        String condition = ReflectionUtils.getFieldValue(component, "getCondition");
        String name = ReflectionUtils.getFieldValue(component, "getName");

        String functionName = getFunctionName(name);

        RelationParser relationParser = new RelationParser(condition, actions, elseActions);
        panelDTO.getComponent().getFunctions().add(relationParser.buildFunction(panelDTO, functionName));
    }

    private void buildHTML(PanelDTO panelDTO, List actions, List elseActions) {
        ArrayList allActions = new ArrayList<>(actions);
        allActions.addAll(elseActions);

        Map<String, String> attToSet = new HashMap<>();

        allActions.forEach(a -> {
            String[] members = a.toString().split(";");

            if (members.length != 6) {
                throw new IllegalArgumentException("Expression must have 6 members");
            }

            if (!"CONTEXT".equals(members[1]) &&  !"ISCLICKED".equals(members[1])) {
                String attribute = getAttributeName(members[1], members[5]);
                attToSet.put(members[0], attribute);
            }
        });

        setAttributes(panelDTO, attToSet);
    }

    private void setAttributes(PanelDTO panelDTO, Map<String, String> attToSet) {
        panelDTO.getHtml().getElements().forEach(e -> attToSet.keySet().forEach(k -> {
            String attribute = attToSet.get(k);
            if (e.canAddRelationAttribute(k, attribute)) {
                String variableName = getVariableName(k, attribute);
                e.addAttribute(attribute, variableName);
                panelDTO.getComponent().checkDeclaration(variableName, getDefaultFieldValue(variableName));
            }
        }));
    }

    public static String getFunctionName(String relationName) {
        String name = relationName.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS, "");
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    public static String getAttributeName(String action, String value) {
        return expressions.get(action.toUpperCase   () + " " + value.toUpperCase());
    }

    public static String getVariableName(String componentName, String attributeName) {

        if (StringUtils.isEmpty(componentName) || StringUtils.isEmpty(attributeName)) {
            return null;
        }

        switch (attributeName) {
            case Constants.READ_ONLY_ATTRIBUTE_NAME:
                return "rdonly" + componentName.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS, "");
            case Constants.HIDDEN_ATTRIBUTE_NAME:
                return "hidden" + componentName.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS, "");
            case Constants.DISABLED_ATTRIBUTE_NAME:
                return "disable" + componentName.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS, "");
            case Constants.REQUIRED_ATTRIBUTE_NAME:
                return "require" + componentName.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS, "");
            default:
                return null;
        }
    }

    private String getDefaultFieldValue(String name) {
        if (name.startsWith("rdonly") || name.startsWith("hidden") || name.startsWith("disable")) {
            return "false";
        }

        if (name.startsWith("require")) {
            return "true";
        }

        return "null";
    }
}
