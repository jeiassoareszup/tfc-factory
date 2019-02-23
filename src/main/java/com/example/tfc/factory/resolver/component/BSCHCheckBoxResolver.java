package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.dto.HTMLElementDTO;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.enums.HTMLElementType;
import com.example.tfc.factory.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

public class BSCHCheckBoxResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

        HTMLElementDTO htmlElementDTO = new HTMLElementDTO();
        htmlElementDTO.setType(HTMLElementType.CHECKBOX);

        htmlElementDTO.addAttribute("[hidden]", String.valueOf(!Boolean.valueOf(ReflectionUtils.getFieldValue(component, "getVisible"))));

        String[] dimensions = StringUtils.split(ReflectionUtils.getFieldValue(component, "getDimensions"), ",");

        htmlElementDTO.addAttribute("style", "position: absolute; left: "+dimensions[0] + "px; top: " + dimensions[1] + "px; width: " + dimensions[2] + "px; height: " + dimensions[3] + "px;");
        htmlElementDTO.addAttribute("class", "my-style");

        String dataName = ReflectionUtils.getFieldValue(component, "getDataName");
        setModelVariable(panelDTO, dataName);

        htmlElementDTO.addAttribute("name", ReflectionUtils.getFieldValue(component, "getName"));
        htmlElementDTO.addAttribute("[(ngModel)]", dataName);

        panelDTO.getHtml().getElements().add(htmlElementDTO);
        HTMLElementDTO div = getDefaultDiv();

        div.getChildren().add(htmlElementDTO);
        return panelDTO;
    }

    private void setModelVariable(PanelDTO panelDTO, String name) {
        panelDTO.getComponent().checkDeclaration(name, "''");
    }
}
