package com.example.tfc.factory.controller;

import com.example.tfc.factory.commons.dto.LoadClassDTO;
import com.example.tfc.factory.service.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadController {

    private LoadService loadService;

    @Autowired
    public LoadController(final LoadService loadService) {
        this.loadService = loadService;
    }

    @PostMapping("/load")
    public void load(@RequestBody LoadClassDTO dto) {
        loadService.load(dto.getClazz());
    }
}
