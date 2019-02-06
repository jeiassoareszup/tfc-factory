package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.dto.PanelDTO;

public abstract class ComponentResolver {

    public abstract PanelDTO resolve(Object component, PanelDTO panelDTO);
}
