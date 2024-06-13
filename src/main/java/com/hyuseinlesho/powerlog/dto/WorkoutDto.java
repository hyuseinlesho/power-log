package com.hyuseinlesho.powerlog.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class WorkoutDto {
    private LocalDate date;
    private Set<ExerciseDto> exercises;
}
