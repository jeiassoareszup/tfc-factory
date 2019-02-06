package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.dto.HTMLElementDTO;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.enums.HTMLElementType;
import com.example.tfc.factory.utils.ReflectionUtils;

public class BSCHButtonComponentResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

        HTMLElementDTO htmlElementDTO = new HTMLElementDTO();

        htmlElementDTO.setType(HTMLElementType.BUTTON);

        panelDTO.getHtml().getElements().add(
                htmlElementDTO
                        .addAttribute("name", ReflectionUtils.getFieldValue(component, "getName"))
                        .addAttribute("text", ReflectionUtils.getFieldValue(component, "getText"))
        );

        return panelDTO;
    }

}
