package com.example.greenlog.service;

import com.example.greenlog.dto.CheckInDTO;
import com.example.greenlog.exception.InvalidCheckInException;
import com.example.greenlog.exception.ResourceNotFoundException;
import com.example.greenlog.model.CheckIn;
import com.example.greenlog.model.Tree;
import com.example.greenlog.repository.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkInRepository;
    private final TreeService treeService;

    @Transactional
    public CheckInDTO create(CheckInDTO dto) {
        Tree tree = treeService.getEntity(dto.getTreeId());
        if ("DEAD".equalsIgnoreCase(tree.getStatus())) {
            throw new InvalidCheckInException("Cannot create check-in because this tree is already marked as DEAD.");
        }

        String status = normalizeStatus(dto.getStatus());
        CheckIn checkIn = new CheckIn();
        checkIn.setCheckInDate(dto.getCheckInDate());
        checkIn.setStatus(status);
        checkIn.setNotes(dto.getNotes());
        checkIn.setTree(tree);
        CheckIn saved = checkInRepository.save(checkIn);

        tree.setStatus(status);
        tree.setNextCheckInDate(dto.getCheckInDate().plusMonths(1));
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CheckInDTO> findAll() {
        return checkInRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CheckInDTO findById(Long id) {
        return toDto(checkInRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in not found with id: " + id)));
    }

    private String normalizeStatus(String status) {
        String value = status.trim().toUpperCase();
        if (!value.equals("ALIVE") && !value.equals("DEAD")) {
            throw new IllegalArgumentException("Check-in status must be ALIVE or DEAD");
        }
        return value;
    }

    private CheckInDTO toDto(CheckIn checkIn) {
        CheckInDTO dto = new CheckInDTO();
        dto.setId(checkIn.getId());
        dto.setTreeId(checkIn.getTree().getId());
        dto.setCheckInDate(checkIn.getCheckInDate());
        dto.setStatus(checkIn.getStatus());
        dto.setNotes(checkIn.getNotes());
        return dto;
    }
}
