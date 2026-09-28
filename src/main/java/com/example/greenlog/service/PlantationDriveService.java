package com.example.greenlog.service;

import com.example.greenlog.dto.PlantationDriveDTO;
import com.example.greenlog.dto.SurvivalRateDTO;
import com.example.greenlog.exception.ResourceNotFoundException;
import com.example.greenlog.model.PlantationDrive;
import com.example.greenlog.repository.PlantationDriveRepository;
import com.example.greenlog.repository.TreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantationDriveService {
    private final PlantationDriveRepository plantationDriveRepository;
    private final TreeRepository treeRepository;

    @Transactional
    public PlantationDriveDTO create(PlantationDriveDTO dto) {
        PlantationDrive drive = new PlantationDrive();
        copyToEntity(dto, drive);
        return toDto(plantationDriveRepository.save(drive));
    }

    @Transactional(readOnly = true)
    public List<PlantationDriveDTO> findAll() {
        return plantationDriveRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PlantationDriveDTO findById(Long id) {
        return toDto(getEntity(id));
    }

    public PlantationDrive findEntity(Long id) {
        return getEntity(id);
    }

    @Transactional
    public PlantationDriveDTO update(Long id, PlantationDriveDTO dto) {
        PlantationDrive drive = getEntity(id);
        copyToEntity(dto, drive);
        return toDto(plantationDriveRepository.save(drive));
    }

    @Transactional
    public void delete(Long id) {
        plantationDriveRepository.delete(getEntity(id));
    }

    @Transactional(readOnly = true)
    public SurvivalRateDTO survivalRate(Long id) {
        getEntity(id);
        long total = treeRepository.countByPlantationDriveId(id);
        long dead = treeRepository.countByPlantationDriveIdAndStatusIgnoreCase(id, "DEAD");
        long alive = total - dead;
        return new SurvivalRateDTO(total, alive, dead, rate(alive, total));
    }

    private PlantationDrive getEntity(Long id) {
        return plantationDriveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plantation drive not found with id: " + id));
    }

    private void copyToEntity(PlantationDriveDTO dto, PlantationDrive drive) {
        drive.setName(dto.getName().trim());
        drive.setLocation(dto.getLocation().trim());
        drive.setDriveDate(dto.getDriveDate());
        drive.setDescription(dto.getDescription());
    }

    private PlantationDriveDTO toDto(PlantationDrive drive) {
        PlantationDriveDTO dto = new PlantationDriveDTO();
        dto.setId(drive.getId());
        dto.setName(drive.getName());
        dto.setLocation(drive.getLocation());
        dto.setDriveDate(drive.getDriveDate());
        dto.setDescription(drive.getDescription());
        return dto;
    }

    private double rate(long alive, long total) {
        return total == 0 ? 0.0 : Math.round((alive * 10000.0 / total)) / 100.0;
    }
}
