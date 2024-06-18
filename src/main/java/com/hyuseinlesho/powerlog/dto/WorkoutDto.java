package com.hyuseinlesho.powerlog.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WorkoutDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot be longer than 100 characters")
    private String title;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Exercises cannot be null")
    @Valid
    private List<ExerciseLogDto> exercises;

    @Size(max = 300, message = "Comment cannot be longer than 300 characters")
    private String comment;
}
