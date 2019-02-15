package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.HTMLElementDTO;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.enums.HTMLElementType;
import com.example.tfc.factory.utils.ReflectionUtils;
import org.springframework.util.StringUtils;

public class BSCHButtonComponentResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

        HTMLElementDTO htmlElementDTO = new HTMLElementDTO();
        htmlElementDTO.setType(HTMLElementType.BUTTON);
        htmlElementDTO.setText(ReflectionUtils.getFieldValue(component, "getText"));
        String name = ReflectionUtils.getFieldValue(component, "getName");

        if(!StringUtils.isEmpty(name)){
            htmlElementDTO.addAttribute("(click)", getClickProcessFunctionName(name) + "()");
        }

        panelDTO.getHtml().getElements().add(
                htmlElementDTO
                        .addAttribute("name", name)
        );

        return panelDTO;
    }

    public String getClickProcessFunctionName(String componentName) {
        if(StringUtils.isEmpty(componentName)){
            return null;
        }
        return "click" + componentName.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS,"");
    }


}
