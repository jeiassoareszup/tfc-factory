package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.dto.HTMLElementDTO;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.dto.TypeScriptFieldDTO;
import com.example.tfc.factory.commons.enums.HTMLElementType;
import com.example.tfc.factory.utils.ReflectionUtils;

public class BSCHTextFieldComponentResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

        HTMLElementDTO htmlElementDTO = new HTMLElementDTO();
        htmlElementDTO.setType(HTMLElementType.INPUT);
        htmlElementDTO.setText(ReflectionUtils.getFieldValue(component, "getText"));

        String variable = ReflectionUtils.getFieldValue(component, "getDataName");
        setModelVariable(panelDTO, variable);

        panelDTO.getHtml().getElements().add(
                htmlElementDTO
                        .addAttribute("name", ReflectionUtils.getFieldValue(component, "getName"))
                        .addAttribute("[(ngModel)]", variable)
                        .addAttribute("maxlength", ReflectionUtils.getFieldValue(component, "getMaxChars"))
        );

        return panelDTO;
    }

    private void setModelVariable(PanelDTO panelDTO, String name){
        panelDTO.getComponent().getFields().add(new TypeScriptFieldDTO(name, "null"));
    }
}
