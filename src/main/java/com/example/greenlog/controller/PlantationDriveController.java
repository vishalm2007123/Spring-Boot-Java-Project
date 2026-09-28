package com.example.greenlog.controller;

import com.example.greenlog.dto.PlantationDriveDTO;
import com.example.greenlog.dto.SurvivalRateDTO;
import com.example.greenlog.service.PlantationDriveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/drives")
@RequiredArgsConstructor
public class PlantationDriveController {
    private final PlantationDriveService service;

    @PostMapping
    public ResponseEntity<PlantationDriveDTO> create(@Valid @RequestBody PlantationDriveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public List<PlantationDriveDTO> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public PlantationDriveDTO findById(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public PlantationDriveDTO update(@PathVariable Long id, @Valid @RequestBody PlantationDriveDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/survival-rate")
    public SurvivalRateDTO survivalRate(@PathVariable Long id) { return service.survivalRate(id); }
}
