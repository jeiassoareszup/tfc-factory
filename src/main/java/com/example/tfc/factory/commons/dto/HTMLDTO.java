package com.example.tfc.factory.commons.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HTMLDTO {
    private List<HTMLElementDTO> elements = new ArrayList<>();
}
