package com.hyuseinlesho.powerlog.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WeightLogResponseDto {
    private Long id;
    private Double weight;
    private LocalDate date;
    private String time;
    private String comment;
}
