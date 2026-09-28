package com.example.greenlog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CheckInDTO {
    private Long id;

    @NotNull(message = "Tree ID is required")
    private Long treeId;

    @NotNull(message = "Check-in date is required")
    private LocalDate checkInDate;

    @NotBlank(message = "Check-in status is required")
    private String status;

    private String notes;
}
