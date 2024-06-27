package com.hyuseinlesho.powerlog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExerciseLogDto {
    private Long id;


    @NotBlank(message = "Exercise is required")
    private String name;

    @Min(value = 1, message = "Sets must be at least 1")
    private Integer sets;

    @Min(value = 1, message = "Reps must be at least 1")
    private Integer reps;

    @Min(value = 0, message = "Weight must be at least 0")
    private Double weight;
}
