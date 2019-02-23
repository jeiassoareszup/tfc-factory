package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.dto.HTMLElementDTO;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.enums.HTMLElementType;
import com.example.tfc.factory.utils.ReflectionUtils;

public class BSCHComboBoxResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

//        String variable = ReflectionUtils.getFieldValue(component, "getDataName");
//
//        HTMLElementDTO htmlElementDTO = new HTMLElementDTO();
//        htmlElementDTO.setType(HTMLElementType.SELECT);
//
//        htmlElementDTO.addAttribute("name", ReflectionUtils.getFieldValue(component, "getName"));
//        htmlElementDTO.addAttribute("label", "Selecione");
//        htmlElementDTO.addAttribute("[options]", variable);
//
//        String dataName = ReflectionUtils.getFieldValue(component, "getDataName");
//        String defaultCode = ReflectionUtils.getFieldValue(component, "getSelectedDefaultCode");
//
//        panelDTO.getComponent().checkDeclaration(dataName, decorateDefaultValue(defaultCode));
//
//        HTMLElementDTO div = super.getDefaultDiv();
//        div.getChildren().add(htmlElementDTO);
//
//        panelDTO.getHtml().getElements().add(div);

        return panelDTO;

    }

    private String decorateDefaultValue(String value) {

        if ("NULL".equals(value) || "FALSE".equals(value) || "TRUE".equals(value)) {
            return value.toLowerCase();
        }

        return "\"" + value + "\"";
    }
}
