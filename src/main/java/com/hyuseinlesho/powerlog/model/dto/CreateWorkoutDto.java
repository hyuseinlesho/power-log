package com.hyuseinlesho.powerlog.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateWorkoutDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot be longer than 100 characters")
    private String title;

    @NotNull(message = "Date and time is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @NotNull(message = "Exercises cannot be null")
    @Valid
    private List<CreateExerciseLogDto> exercises;

    @Size(max = 300, message = "Comment cannot be longer than 300 characters")
    private String comment;
}
