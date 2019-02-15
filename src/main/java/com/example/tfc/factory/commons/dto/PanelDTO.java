package com.example.tfc.factory.commons.dto;

import lombok.Data;

@Data
public class PanelDTO {

    private String name;
    private String title;
    private HTMLDTO html;
    private TypeScriptComponentDTO component;

    public static PanelDTO init() {

        PanelDTO panelDTO = new PanelDTO();

        // html
        HTMLDTO htmldto = new HTMLDTO();
        panelDTO.setHtml(htmldto);

        // component
        TypeScriptComponentDTO typeScriptComponentDTO = new TypeScriptComponentDTO();
        panelDTO.setComponent(typeScriptComponentDTO);

        return panelDTO;
    }

}
