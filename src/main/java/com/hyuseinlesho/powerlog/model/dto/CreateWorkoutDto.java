package com.hyuseinlesho.powerlog.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWorkoutDto {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot be longer than 100 characters")
    private String title;

    @NotNull(message = "Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotBlank(message = "Time is required")
    private String time;

    @NotNull(message = "Exercises cannot be null")
    @Valid
    private List<ExerciseLogDto> exercises;

    @Size(max = 300, message = "Comment cannot be longer than 300 characters")
    private String comment;
}
