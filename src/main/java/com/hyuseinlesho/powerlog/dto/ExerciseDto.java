package com.hyuseinlesho.powerlog.dto;

import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseDto {
    private Long id;
    public String name;
    public ExerciseType type;
}
