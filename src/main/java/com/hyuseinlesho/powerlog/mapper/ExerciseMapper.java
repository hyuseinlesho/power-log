package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.model.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExerciseMapper {
    ExerciseMapper INSTANCE = Mappers.getMapper(ExerciseMapper.class);

    Exercise mapToExercise(CreateExerciseDto exerciseDto);

    CreateExerciseDto mapExerciseDto(Exercise exercise);
}
