package com.hyuseinlesho.powerlog.dto;

import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseDto {
    private Long id;

    @NotBlank(message = "Name is required")
    public String name;

    @NotNull(message = "Type is required")
    public ExerciseType type;
}
