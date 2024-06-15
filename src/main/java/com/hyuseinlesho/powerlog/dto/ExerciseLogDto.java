package com.hyuseinlesho.powerlog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseLogDto {
    private Long id;
    private String name;
    private int sets;
    private int reps;
    private double weight;
}
