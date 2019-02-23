package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.dto.HTMLElementDTO;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.enums.HTMLElementType;

public abstract class ComponentResolver {

    public abstract PanelDTO resolve(Object component, PanelDTO panelDTO);

    HTMLElementDTO getDefaultDiv(){
        HTMLElementDTO elementDTO = new HTMLElementDTO();
        elementDTO.setType(HTMLElementType.DIV);
        elementDTO.addAttribute("class", "col-xs-3 my-div");
        return elementDTO;
    }
}
