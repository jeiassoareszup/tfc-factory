package com.example.tfc.factory.commons.dto;

import java.util.ArrayList;
import java.util.List;

public class HTMLDTO {
    private String fileName;
    private List<HTMLElementDTO> elements = new ArrayList<>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<HTMLElementDTO> getElements() {
        return elements;
    }

    public void setElements(List<HTMLElementDTO> elements) {
        this.elements = elements;
    }
}
