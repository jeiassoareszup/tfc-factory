package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.dto.HTMLElementDTO;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.enums.HTMLElementType;
import com.example.tfc.factory.utils.ReflectionUtils;

import java.util.List;

public class BSCHTableComponentResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

        HTMLElementDTO table = new HTMLElementDTO();
        table.setType(HTMLElementType.TABLE);
        table.addAttribute("name", ReflectionUtils.getFieldValue(component, "getName"));

        List columns = ReflectionUtils.getListField(component, "getColumns");


        HTMLElementDTO headerRow = new HTMLElementDTO();
        headerRow.setType(HTMLElementType.TABLE_ROW);
        table.getElements().add(headerRow);

        HTMLElementDTO elementRow = new HTMLElementDTO();
        String tableSource = ReflectionUtils.getFieldValue(component, "getDataNameForTable");
        elementRow.addAttribute("*ngFor", "element of " + tableSource);
        elementRow.setType(HTMLElementType.TABLE_ROW);
        table.getElements().add(elementRow);

        columns.forEach(c -> {

            HTMLElementDTO header = new HTMLElementDTO();
            header.setType(HTMLElementType.TABLE_HEADER);

            HTMLElementDTO headerText = new HTMLElementDTO();
            headerText.setType(HTMLElementType.TEXT);
            headerText.setContent(c.toString().split(";")[0]);
            header.getElements().add(headerText);

            headerRow.getElements().add(header);

            HTMLElementDTO rowData = new HTMLElementDTO();
            rowData.setType(HTMLElementType.TABLE_DATA);

            HTMLElementDTO elementText = new HTMLElementDTO();
            elementText.setType(HTMLElementType.TEXT);
            elementText.setContent("{{element." + c.toString().split(";")[2] + "}}");
            rowData.getElements().add(elementText);

            elementRow.getElements().add(rowData);
        });


        panelDTO.getHtml().getElements().add(table);

        return panelDTO;
    }

}
