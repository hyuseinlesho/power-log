package com.hyuseinlesho.powerlog.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WorkoutDto {
    private Long id;
    private LocalDate date;
    private List<ExerciseLogDto> exercises;
}
