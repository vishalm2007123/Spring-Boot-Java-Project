package com.example.greenlog.service;

import com.example.greenlog.dto.LeaderboardEntryDTO;
import com.example.greenlog.dto.VolunteerDTO;
import com.example.greenlog.exception.ResourceNotFoundException;
import com.example.greenlog.model.Volunteer;
import com.example.greenlog.repository.TreeRepository;
import com.example.greenlog.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;
    private final TreeRepository treeRepository;

    @Transactional
    public VolunteerDTO create(VolunteerDTO dto) {
        Volunteer volunteer = new Volunteer();
        copyToEntity(dto, volunteer);
        return toDto(volunteerRepository.save(volunteer));
    }

    @Transactional(readOnly = true)
    public List<VolunteerDTO> findAll() {
        return volunteerRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public VolunteerDTO findById(Long id) {
        return toDto(getEntity(id));
    }

    @Transactional
    public VolunteerDTO update(Long id, VolunteerDTO dto) {
        Volunteer volunteer = getEntity(id);
        copyToEntity(dto, volunteer);
        return toDto(volunteerRepository.save(volunteer));
    }

    @Transactional
    public void delete(Long id) {
        volunteerRepository.delete(getEntity(id));
    }

    @Transactional(readOnly = true)
    public List<LeaderboardEntryDTO> leaderboard() {
        return volunteerRepository.findAll().stream()
                .map(volunteer -> new LeaderboardEntryDTO(volunteer.getId(), volunteer.getName(),
                        treeRepository.countByVolunteerId(volunteer.getId())))
                .sorted(Comparator.comparingLong(LeaderboardEntryDTO::getTreesPlanted).reversed()
                        .thenComparing(LeaderboardEntryDTO::getVolunteerName))
                .toList();
    }

    public Volunteer getEntity(Long id) {
        return volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));
    }

    private void copyToEntity(VolunteerDTO dto, Volunteer volunteer) {
        volunteer.setName(dto.getName().trim());
        volunteer.setEmail(dto.getEmail().trim());
        volunteer.setPhone(dto.getPhone());
    }

    private VolunteerDTO toDto(Volunteer volunteer) {
        VolunteerDTO dto = new VolunteerDTO();
        dto.setId(volunteer.getId());
        dto.setName(volunteer.getName());
        dto.setEmail(volunteer.getEmail());
        dto.setPhone(volunteer.getPhone());
        return dto;
    }
}
