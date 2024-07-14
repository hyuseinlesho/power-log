package com.hyuseinlesho.powerlog.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseLogGraphDto {
    private String exerciseName;
    private int sets;
    private int reps;
    private double weight;
    private LocalDate date;
    private String time;
}
