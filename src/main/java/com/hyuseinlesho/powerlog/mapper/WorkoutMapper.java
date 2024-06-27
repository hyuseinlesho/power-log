package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkoutMapper {
    WorkoutMapper INSTANCE = Mappers.getMapper(WorkoutMapper.class);

    Workout mapToWorkout(CreateWorkoutDto workoutDto);

    CreateWorkoutDto mapToWorkoutDto(Workout workout);
}
