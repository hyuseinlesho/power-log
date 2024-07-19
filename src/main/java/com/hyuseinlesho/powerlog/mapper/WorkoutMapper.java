package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.model.dto.CreateWorkoutDto;
import com.hyuseinlesho.powerlog.model.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.entity.Workout;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkoutMapper {
    Workout mapToWorkout(CreateWorkoutDto workoutDto);

    WorkoutDto mapToWorkoutDto(Workout workout);
}
