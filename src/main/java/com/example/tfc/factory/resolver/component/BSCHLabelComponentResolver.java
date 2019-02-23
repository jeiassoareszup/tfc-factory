package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.utils.ReflectionUtils;

public class BSCHLabelComponentResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

        String text = ReflectionUtils.getFieldValue(component, "getText");
        String name = ReflectionUtils.getFieldValue(component, "getName");

        panelDTO.getComponent().checkDeclaration(getLabelVariableName(name.substring(2)), "'" + (text.length() > 0 ? text : ".") + "'");

        return panelDTO;
    }

    public static String getLabelVariableName(String componentName) {
        return "label" + componentName.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS, "");
    }
}
