package com.example.tfc.factory.parser;

import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.dto.TypeScriptFunctionDTO;
import com.example.tfc.factory.utils.TypeScriptTemplateUtils;
import lombok.Getter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

        String parsedCondition = new ConditionExpressionParser(this.condition).evaluate();

        StringBuilder ifBody = new StringBuilder();
        StringBuilder elseBody = new StringBuilder();

        this.actions.forEach(a -> {
            ActionExpressionParser parser = new ActionExpressionParser(a);
            ifBody.append(parser.evaluate());
            if("CONTEXT".equals(parser.getParameter())){
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
}
