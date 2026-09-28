package com.example.greenlog.controller;

import com.example.greenlog.dto.LeaderboardEntryDTO;
import com.example.greenlog.dto.VolunteerDTO;
import com.example.greenlog.service.VolunteerService;
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
@RequestMapping("/api/volunteers")
@RequiredArgsConstructor
public class VolunteerController {
    private final VolunteerService service;

    @PostMapping
    public ResponseEntity<VolunteerDTO> create(@Valid @RequestBody VolunteerDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public List<VolunteerDTO> findAll() { return service.findAll(); }

    @GetMapping("/leaderboard")
    public List<LeaderboardEntryDTO> leaderboard() { return service.leaderboard(); }

    @GetMapping("/{id}")
    public VolunteerDTO findById(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public VolunteerDTO update(@PathVariable Long id, @Valid @RequestBody VolunteerDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
