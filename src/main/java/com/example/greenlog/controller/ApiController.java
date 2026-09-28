package com.example.greenlog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiController {

    @GetMapping("/api/info")
    public Map<String, String> home() {
        return Map.of(
                "application", "GreenLog",
                "message", "Tree Plantation Drive Tracker API is running",
                "apiBaseUrl", "/api"
        );
    }
}
