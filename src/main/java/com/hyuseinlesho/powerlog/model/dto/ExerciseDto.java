package com.hyuseinlesho.powerlog.model.dto;

import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseDto {
    private Long id;
    private String name;
    private ExerciseType type;
}
