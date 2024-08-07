package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.ExerciseResponseDto;
import com.hyuseinlesho.powerlog.model.entity.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExerciseMapper {
    Exercise mapToExercise(CreateExerciseDto exerciseDto);

    ExerciseDto mapToExerciseDto(Exercise exercise);

    ExerciseResponseDto mapToExerciseResponseDto(Exercise exercise);
}
