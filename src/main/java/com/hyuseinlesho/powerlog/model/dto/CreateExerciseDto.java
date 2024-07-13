package com.hyuseinlesho.powerlog.model.dto;

import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateExerciseDto {

    @NotBlank(message = "Name is required")
    public String name;

    @NotNull(message = "Type is required")
    public ExerciseType type;
}
