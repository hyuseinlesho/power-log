package com.hyuseinlesho.powerlog.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WorkoutDto {
    private Long id;
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String time;
    private List<CreateExerciseLogDto> exercises;
    private double totalVolume;
    private String comment;
}
