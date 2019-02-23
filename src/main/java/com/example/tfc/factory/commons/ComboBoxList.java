package com.example.tfc.factory.commons;

public enum ComboBoxList {

    BRBW001TIPCONTA("[{value: '1', label: 'LIVRE MOVIMENTAÇÃO'},\n" +
            "            {value: '2', label: 'CONTA SALÁRIO'},\n" +
            "            {value: '3', label: 'CONTA INTERNA'}]"),
    BRBCSIMNAO("[{value: 'S', label: 'SIM'},\n" +
            "    {value: 'N', label: 'NÃO'}]"),
    BRBC899TIPODIA("[{value: 'A', label: 'ANIVERSÁRIO'},\n" +
            "            {value: 'N', label: 'DIA FIXO'}]"),
    BRBC899TIPOMOV("[{label: 'I', value: 'INDIVIDUAL'},\n" +
            "    {value: 'C', label: 'CONJUNTA SOLIDÁRIA (E/OU)'},\n" +
            "    {value: 'L', label: 'CONJUNTA NÃO SOLIDARIA (E)'}]"),
    BRBWSIMNAO("[{value: 'S', label: 'SIM'},\n" +
            "    {value: 'N', label: 'NÃO'}]");

    private String value;

    ComboBoxList(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
