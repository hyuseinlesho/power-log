package com.hyuseinlesho.powerlog.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoutineDto {
    private Long id;
    private String name;
    private List<RoutineExerciseDto> exercises;
}
