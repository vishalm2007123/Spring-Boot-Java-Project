package com.example.greenlog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TreeDTO {
    private Long id;

    @NotBlank(message = "Tree species is required")
    private String species;

    @NotBlank(message = "Tree location is required")
    private String location;

    @NotNull(message = "Date planted is required")
    private LocalDate datePlanted;

    private String status;
    private LocalDate nextCheckInDate;

    @NotNull(message = "Plantation drive ID is required")
    private Long plantationDriveId;

    private Long volunteerId;
}
