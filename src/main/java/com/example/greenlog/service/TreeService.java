package com.example.greenlog.service;

import com.example.greenlog.dto.SurvivalRateDTO;
import com.example.greenlog.dto.TreeDTO;
import com.example.greenlog.exception.ResourceNotFoundException;
import com.example.greenlog.model.PlantationDrive;
import com.example.greenlog.model.Tree;
import com.example.greenlog.model.Volunteer;
import com.example.greenlog.repository.TreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreeService {
    private final TreeRepository treeRepository;
    private final PlantationDriveService plantationDriveService;
    private final VolunteerService volunteerService;

    @Transactional
    public TreeDTO create(TreeDTO dto) {
        Tree tree = new Tree();
        copyToEntity(dto, tree);
        tree.setStatus(normalizeStatus(dto.getStatus(), "ALIVE"));
        return toDto(treeRepository.save(tree));
    }

    @Transactional(readOnly = true)
    public List<TreeDTO> findAll() {
        return treeRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public TreeDTO findById(Long id) {
        return toDto(getEntity(id));
    }

    @Transactional
    public TreeDTO update(Long id, TreeDTO dto) {
        Tree tree = getEntity(id);
        copyToEntity(dto, tree);
        tree.setStatus(normalizeStatus(dto.getStatus(), tree.getStatus()));
        return toDto(treeRepository.save(tree));
    }

    @Transactional
    public void delete(Long id) {
        treeRepository.delete(getEntity(id));
    }

    @Transactional(readOnly = true)
    public List<TreeDTO> dueCheckIns() {
        return treeRepository.findByNextCheckInDateLessThanEqualAndStatusNotIgnoreCase(LocalDate.now(), "DEAD")
                .stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public SurvivalRateDTO survivalRateBySpecies(String species) {
        long total = treeRepository.countBySpeciesIgnoreCase(species);
        long dead = treeRepository.countBySpeciesIgnoreCaseAndStatusIgnoreCase(species, "DEAD");
        long alive = total - dead;
        return new SurvivalRateDTO(total, alive, dead, total == 0 ? 0.0 : Math.round(alive * 10000.0 / total) / 100.0);
    }

    public Tree getEntity(Long id) {
        return treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id: " + id));
    }

    private void copyToEntity(TreeDTO dto, Tree tree) {
        PlantationDrive drive = plantationDriveService.findEntity(dto.getPlantationDriveId());
        Volunteer volunteer = dto.getVolunteerId() == null ? null : volunteerService.getEntity(dto.getVolunteerId());
        tree.setSpecies(dto.getSpecies().trim());
        tree.setLocation(dto.getLocation().trim());
        tree.setDatePlanted(dto.getDatePlanted());
        tree.setNextCheckInDate(dto.getNextCheckInDate());
        tree.setPlantationDrive(drive);
        tree.setVolunteer(volunteer);
    }

    private String normalizeStatus(String status, String fallback) {
        String value = status == null || status.isBlank() ? fallback : status.trim().toUpperCase();
        if (!value.equals("ALIVE") && !value.equals("DEAD")) {
            throw new IllegalArgumentException("Tree status must be ALIVE or DEAD");
        }
        return value;
    }

    private TreeDTO toDto(Tree tree) {
        TreeDTO dto = new TreeDTO();
        dto.setId(tree.getId());
        dto.setSpecies(tree.getSpecies());
        dto.setLocation(tree.getLocation());
        dto.setDatePlanted(tree.getDatePlanted());
        dto.setStatus(tree.getStatus());
        dto.setNextCheckInDate(tree.getNextCheckInDate());
        dto.setPlantationDriveId(tree.getPlantationDrive().getId());
        dto.setVolunteerId(tree.getVolunteer() == null ? null : tree.getVolunteer().getId());
        return dto;
    }
}
