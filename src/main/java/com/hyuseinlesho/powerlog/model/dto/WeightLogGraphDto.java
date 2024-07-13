package com.hyuseinlesho.powerlog.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeightLogGraphDto {
    private Double weight;
    private LocalDate date;
    private String time;
}
