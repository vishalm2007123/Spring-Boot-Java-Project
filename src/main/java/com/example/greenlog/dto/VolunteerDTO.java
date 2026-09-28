package com.example.greenlog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VolunteerDTO {
    private Long id;

    @NotBlank(message = "Volunteer name is required")
    private String name;

    @NotBlank(message = "Volunteer email is required")
    @Email(message = "Volunteer email must be valid")
    private String email;

    private String phone;
}
