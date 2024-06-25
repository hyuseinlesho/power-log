package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkoutMapper {
    WorkoutMapper INSTANCE = Mappers.getMapper(WorkoutMapper.class);

    Workout mapToWorkout(WorkoutDto workoutDto);

    WorkoutDto mapToWorkoutDto(Workout workout);
}
