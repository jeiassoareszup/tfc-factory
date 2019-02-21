package com.example.tfc.factory.commons.dto;

public class PanelDTO {

    private String name;
    private String title;
    private HTMLDTO html;
    private TypeScriptComponentDTO component;
    private ServiceDTO service;

    public static PanelDTO init() {

        PanelDTO panelDTO = new PanelDTO();

        // html
        HTMLDTO htmldto = new HTMLDTO();
        panelDTO.setHtml(htmldto);

        // component
        TypeScriptComponentDTO typeScriptComponentDTO = new TypeScriptComponentDTO();
        panelDTO.setComponent(typeScriptComponentDTO);

        panelDTO.setService(new ServiceDTO());
        return panelDTO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HTMLDTO getHtml() {
        return html;
    }

    public void setHtml(HTMLDTO html) {
        this.html = html;
    }

    public TypeScriptComponentDTO getComponent() {
        return component;
    }

    public void setComponent(TypeScriptComponentDTO component) {
        this.component = component;
    }

    public ServiceDTO getService() {
        return service;
    }

    public void setService(ServiceDTO service) {
        this.service = service;
    }
}
