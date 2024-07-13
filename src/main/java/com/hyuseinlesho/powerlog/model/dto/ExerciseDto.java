package com.hyuseinlesho.powerlog.model.dto;

import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseDto {
    private Long id;
    private String name;
    private ExerciseType type;
}
