package com.example.greenlog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SurvivalRateDTO {
    private long totalTrees;
    private long aliveTrees;
    private long deadTrees;
    private double survivalRate;
}
