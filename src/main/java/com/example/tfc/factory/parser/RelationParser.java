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

        if(StringUtils.isEmpty(condition) || StringUtils.isEmpty(actions)){
            throw new IllegalArgumentException("condition and actions can not be empty");
        }

        this.condition = condition;
        this.actions = actions;
        this.elseActions = elseActions;
    }

    public TypeScriptFunctionDTO buildFunction(String name, PanelDTO panelDTO){

        String type = condition.split(";")[1];

        if("ISCLICKED".equals(type)){
            StringBuilder body = new StringBuilder();
            actions.forEach(a -> body.append(new ActionExpressionParser(a).evaluate()));
            return new TypeScriptFunctionDTO(name, null, body.toString());
        }

        String parsedCondition = new ConditionExpressionParser(this.condition).evaluate();

        StringBuilder ifBody = new StringBuilder();
        StringBuilder elseBody = new StringBuilder();

        this.actions.forEach(a -> ifBody.append(new ActionExpressionParser(a).evaluate()));

        if(!CollectionUtils.isEmpty(this.elseActions)){
            this.elseActions.forEach(a -> elseBody.append(new ActionExpressionParser(a).evaluate()));
        }

        String body = TypeScriptTemplateUtils.getIf(parsedCondition, ifBody.toString(), elseBody.toString());

        return new TypeScriptFunctionDTO(name, null, body);
    }

}
