package com.example.greenlog.controller;

import com.example.greenlog.dto.SurvivalRateDTO;
import com.example.greenlog.dto.TreeDTO;
import com.example.greenlog.service.TreeService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trees")
@RequiredArgsConstructor
public class TreeController {
    private final TreeService service;

    @PostMapping
    public ResponseEntity<TreeDTO> create(@Valid @RequestBody TreeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public List<TreeDTO> findAll() { return service.findAll(); }

    @GetMapping("/due-checkins")
    public List<TreeDTO> dueCheckIns() { return service.dueCheckIns(); }

    @GetMapping("/survival-rate/species/{species}")
    public SurvivalRateDTO survivalRateBySpecies(@PathVariable String species) {
        return service.survivalRateBySpecies(species);
    }

    @GetMapping("/{id}")
    public TreeDTO findById(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public TreeDTO update(@PathVariable Long id, @Valid @RequestBody TreeDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
