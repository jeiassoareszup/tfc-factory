package com.example.tfc.factory.commons.dto;

import lombok.Data;

@Data
public class PanelDTO {

    private String name;
    private String title;
    private HTMLDTO html;
    private ComponentDTO component;

    public static PanelDTO init() {

        PanelDTO panelDTO = new PanelDTO();

        // html
        HTMLDTO htmldto = new HTMLDTO();
        panelDTO.setHtml(htmldto);

        // component
        TypeScriptComponentDTO typeScriptComponentDTO = new TypeScriptComponentDTO();
        ComponentDTO componentDTO = new ComponentDTO();
        componentDTO.setTs(typeScriptComponentDTO);
        panelDTO.setComponent(componentDTO);

        return panelDTO;
    }

}
