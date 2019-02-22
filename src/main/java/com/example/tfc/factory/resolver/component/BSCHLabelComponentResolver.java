package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.dto.HTMLElementDTO;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.enums.HTMLElementType;
import com.example.tfc.factory.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

public class BSCHLabelComponentResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

        HTMLElementDTO htmlElementDTO = new HTMLElementDTO();
        htmlElementDTO.setType(HTMLElementType.LABEL);
        htmlElementDTO.setText(ReflectionUtils.getFieldValue(component, "getText"));

        String[] dimensions = StringUtils.split(ReflectionUtils.getFieldValue(component, "getDimensions"), ",");

        htmlElementDTO.addAttribute("style", "position: absolute; left: "+dimensions[0] + "px; top: " + dimensions[1] + "px; width: " + dimensions[2] + "px; height: " + dimensions[3] + "px;");

        panelDTO.getHtml().getElements().add(
                htmlElementDTO
                        .addAttribute("name", ReflectionUtils.getFieldValue(component, "getName"))
        );

        return panelDTO;
    }
}
