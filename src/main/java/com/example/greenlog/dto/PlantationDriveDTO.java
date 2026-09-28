package com.example.greenlog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlantationDriveDTO {
    private Long id;

    @NotBlank(message = "Drive name is required")
    private String name;

    @NotBlank(message = "Drive location is required")
    private String location;

    @NotNull(message = "Drive date is required")
    private LocalDate driveDate;

    private String description;
}
