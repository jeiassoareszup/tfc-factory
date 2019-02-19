package com.example.tfc.factory.writer;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.HTMLElementDTO;
import com.example.tfc.factory.commons.dto.PanelDTO;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.StringReader;

@Component
@Qualifier("htmlWriter")
public class HTMLWriter implements Writer {

    @Override
    public void write(PanelDTO panelDTO) {

        String xml = "<div>" +
                "</div>";

        SAXBuilder builder = new SAXBuilder();

        try {
            Document doc = builder.build(new StringReader(xml));
            panelDTO.getHtml().getElements().forEach(e -> writeHtmlElements(e, doc.getRootElement()));

            Format format = Format.getPrettyFormat();
            format.setOmitDeclaration(true);

            XMLOutputter outputter = new XMLOutputter(format);
            panelDTO.getHtml().setFileName(panelDTO.getName()+ ".html");

            outputter.output(doc, new FileWriter(Constants.FULL_COMPONENT_FOLDER_PATH + panelDTO.getName() + "/" + panelDTO.getHtml().getFileName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void writeHtmlElements(HTMLElementDTO elementDTO, Element parent) {

        Element element = new Element(elementDTO.getType().getTagName());
        element.setAttributes(elementDTO.getAttributes());
        element.setText(elementDTO.getText());

        parent.addContent(element);

        elementDTO.getChildren().forEach(e -> writeHtmlElements(e, element));
    }

}
