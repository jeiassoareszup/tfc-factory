package com.example.tfc.factory.resolver.field;

import com.example.tfc.factory.commons.dto.PanelDTO;

public abstract class FieldResolver {

    public abstract PanelDTO resolve(Object field, PanelDTO panelDTO);

}
