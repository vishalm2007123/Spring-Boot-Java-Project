package com.example.greenlog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardEntryDTO {
    private Long volunteerId;
    private String volunteerName;
    private long treesPlanted;
}
