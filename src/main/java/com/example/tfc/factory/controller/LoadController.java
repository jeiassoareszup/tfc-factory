package com.example.tfc.factory.controller;

import com.example.tfc.factory.service.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadController {

    private LoadService loadService;

    @Autowired
    public LoadController(final LoadService loadService) {
        this.loadService = loadService;
    }

    @GetMapping("/load")
    public void load() {
        loadService.load();
    }
}
