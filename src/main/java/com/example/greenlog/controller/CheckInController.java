package com.example.greenlog.controller;

import com.example.greenlog.dto.CheckInDTO;
import com.example.greenlog.service.CheckInService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/checkins")
@RequiredArgsConstructor
public class CheckInController {
    private final CheckInService service;

    @PostMapping
    public ResponseEntity<CheckInDTO> create(@Valid @RequestBody CheckInDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public List<CheckInDTO> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public CheckInDTO findById(@PathVariable Long id) { return service.findById(id); }
}
