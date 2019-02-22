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
        table.getChildren().add(headerRow);

        HTMLElementDTO elementRow = new HTMLElementDTO();
        String tableSource = ReflectionUtils.getFieldValue(component, "getDataNameForTable");
        elementRow.addAttribute("*ngFor", "let element of " + tableSource);
        elementRow.setType(HTMLElementType.TABLE_ROW);
        table.getChildren().add(elementRow);

        columns.forEach(c -> {

            HTMLElementDTO header = new HTMLElementDTO();
            header.setType(HTMLElementType.TABLE_HEADER);
            header.setText(c.toString().split(";")[0]);

            headerRow.getChildren().add(header);

            HTMLElementDTO rowData = new HTMLElementDTO();
            rowData.setType(HTMLElementType.TABLE_DATA);
            rowData.setText("{{element." + c.toString().split(";")[2] + "}}");

            elementRow.getChildren().add(rowData);
        });


        panelDTO.getHtml().getElements().add(table);

        return panelDTO;
    }

}
