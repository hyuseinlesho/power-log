package com.hyuseinlesho.powerlog.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseLogDto {
    private String name;
    private Integer sets;
    private Integer reps;
    private Double weight;
}
