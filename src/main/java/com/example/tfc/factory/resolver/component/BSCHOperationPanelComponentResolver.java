package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.utils.ReflectionUtils;

public class BSCHOperationPanelComponentResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

        panelDTO.setName(ReflectionUtils.getFieldValue(component, "getName"));
        panelDTO.setTitle(ReflectionUtils.getFieldValue(component, "getTitle"));
        return panelDTO;
    }
}
