package com.example.tfc.factory.parser;

import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.dto.TypeScriptFunctionDTO;
import com.example.tfc.factory.utils.TypeScriptTemplateUtils;
import lombok.Getter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
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

    public TypeScriptFunctionDTO buildFunction(PanelDTO panelDTO, String name) {

        String type = condition.split(";")[1];

        if ("ISCLICKED".equals(type)) {
            StringBuilder body = new StringBuilder();
            actions.forEach(a -> {
                ActionExpressionParser parser = new ActionExpressionParser(a);
                body.append(parser.evaluate());
                panelDTO.getComponent().checkDeclaration(parser.getField().getName(), parser.getField().getValue());
            });
            return new TypeScriptFunctionDTO("", name, null, body.toString(), null);
        }

        String parsedCondition = parseCondition();

        StringBuilder ifBody = new StringBuilder();
        StringBuilder elseBody = new StringBuilder();

        this.actions.forEach(a -> {
            ActionExpressionParser parser = new ActionExpressionParser(a);
            ifBody.append(parser.evaluate());
            if ("CONTEXT".equals(parser.getParameter())) {
                panelDTO.getComponent().checkDeclaration(parser.getOutput(), "null");
            }
            panelDTO.getComponent().checkDeclaration(parser.getField().getName(), parser.getField().getValue());
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

    private String parseCondition() {

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

                builder.append(new ConditionExpressionParser(conditions[i]).evaluate());

                if (i < conditions.length - 1) {
                    builder.append(" ");
                    builder.append(ops.get(i));
                    builder.append(" ");
                }
            }

            return builder.toString();
        }


        return new ConditionExpressionParser(this.condition).evaluate();
    }
}
