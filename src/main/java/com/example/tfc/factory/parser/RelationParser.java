package com.example.tfc.factory.parser;

import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.dto.TypeScriptFunctionDTO;
import com.example.tfc.factory.utils.TypeScriptTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RelationParser {

    private String condition;
    private List<String> actions;
    private List<String> elseActions;

    public RelationParser(String condition, List<String> actions, List<String> elseActions) {

        if (StringUtils.isEmpty(condition) || StringUtils.isEmpty(actions)) {
            throw new IllegalArgumentException("condition and actions can not be empty");
        }

        this.condition = condition;
        this.actions = actions;
        this.elseActions = elseActions;
    }

    public String getCondition() {
        return condition;
    }

    public List<String> getActions() {
        return actions;
    }

    public List<String> getElseActions() {
        return elseActions;
    }

    public TypeScriptFunctionDTO buildFunction(PanelDTO panelDTO, String name) {

        String type = condition.split(";")[1];

        if ("ISCLICKED".equals(type)) {
            StringBuilder body = new StringBuilder();
            actions.forEach(a -> {
                ActionExpressionParser parser = new ActionExpressionParser(a);
                body.append(parser.evaluate());
                panelDTO.getComponent().checkDeclaration(parser.getField().getName(), parser.getField().getValue());
                if (parser.getField().getValue().startsWith("this.")) {
                    panelDTO.getComponent().checkDeclaration(parser.getField().getValue().substring(5), "null");
                }
            });
            return new TypeScriptFunctionDTO("", name, null, body.toString(), null);
        }

        String parsedCondition = parseCondition(panelDTO, type);

        StringBuilder ifBody = new StringBuilder();
        StringBuilder elseBody = new StringBuilder();

        this.actions.forEach(a -> {
            ActionExpressionParser parser = new ActionExpressionParser(a);
            String evaluated = parser.evaluate();
            if (!StringUtils.isEmpty(evaluated)) {
                ifBody.append(evaluated);
                if ("CONTEXT".equals(parser.getParameter())) {
                    panelDTO.getComponent().checkDeclaration(parser.getOutput(), "null");
                }
                panelDTO.getComponent().checkDeclaration(parser.getField().getName(), parser.getField().getValue());
            }
        });

        if (!CollectionUtils.isEmpty(this.elseActions)) {
            this.elseActions.forEach(a -> {
                ActionExpressionParser parser = new ActionExpressionParser(a);
                elseBody.append(parser.evaluate());
                panelDTO.getComponent().checkDeclaration(parser.getField().getName(), parser.getField().getValue());
            });
        }

        String body = TypeScriptTemplateUtils.getIf(parsedCondition, ifBody.toString(), elseBody.toString());

        return new TypeScriptFunctionDTO("", name, null, body, null);
    }

    // TODO: modificar type para funcoes com ||
    private String parseCondition(PanelDTO panelDTO, String type) {

        if (this.condition.contains("||") || this.condition.contains("&&")) {

            String[] conditions = condition.split("\\|{2}|\\&{2}");

            List<String> ops = new ArrayList<>();

            String acc = "";
            for (int i = 0; i < conditions.length - 1; i++) {
                String op = this.condition.substring(acc.length() + conditions[i].length(), acc.length() + conditions[i].length() + 2);
                ops.add(op);
                acc = acc.concat(conditions[i]);
                acc = acc.concat(op);
            }

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < conditions.length; i++) {

                String[] parsed = conditions[i].split(";");

                if ("CONTEXT".equals(parsed[1])) {
                    String[] split = conditions[i].split(";");
                    checkAttribution(panelDTO, split);
                }

                builder.append(new ConditionExpressionParser(conditions[i]).evaluate());

                if (i < conditions.length - 1) {
                    builder.append(" ");
                    builder.append(ops.get(i));
                    builder.append(" ");
                }
            }

            String result = builder.toString();

            if (result.endsWith("|| ")) {
                result = result.substring(0, result.length() - 4);
            }

            return result;
        }


        if ("CONTEXT".equals(type)) {
            String[] split = this.condition.split(";");
            checkAttribution(panelDTO, split);
        }

        return new ConditionExpressionParser(this.condition).evaluate();
    }

    private void checkAttribution(PanelDTO panelDTO, String[] split) {
        String value;

        if ("CONTEXT".equals(split[5])) {
            value = "this." + split[4];
        } else {
            value = split[5];
        }

        panelDTO.getComponent().checkDeclaration(split[0], decorateValue(value, split[2], split[4]));
    }

    private String decorateValue(String value, String parameterType, String output) {

        if ("ALPHANUMERIC".equals(parameterType) && "VALUE".equals(output)) {

            if ("NULL".equals(value)) {
                return value.toLowerCase();
            }

            return "\"" + value + "\"";
        }
        return value;
    }
}
