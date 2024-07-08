package com.hyuseinlesho.powerlog.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExerciseLogGraphDto {
    private String exerciseName;
    private int sets;
    private int reps;
    private double weight;
    private LocalDate date;
    private String time;

    public ExerciseLogGraphDto(String exerciseName, int sets, int reps, double weight, LocalDate date, String time) {
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.date = date;
        this.time = time;
    }
}
