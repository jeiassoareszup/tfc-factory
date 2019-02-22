package com.example.tfc.factory.commons.enums;

public enum HTMLElementType {
    LABEL("label"),
    DIV("div"),
    BUTTON("button"),
    CHECKBOX("spaui-checkbox"),
    TABLE("table"),
    INPUT("spaui-input"),
    SELECT("spaui-select"),
    NG_TEMPLATE("ng-template"),
    TABLE_HEADER("th"),
    TABLE_DATA("td"),
    TABLE_ROW("tr");

    private String tagName;

    HTMLElementType(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {

        if (tagName == null) {
            throw new IllegalStateException();
        }

        return tagName;
    }
}
