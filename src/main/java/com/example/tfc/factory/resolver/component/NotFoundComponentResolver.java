package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.dto.PanelDTO;

public class NotFoundComponentResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {
        return panelDTO;
    }

}
