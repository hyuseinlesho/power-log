package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.model.dto.ExerciseLogGraphDto;
import com.hyuseinlesho.powerlog.model.entity.ExerciseLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ExerciseLogMapper {
    @Mapping(source = "workout.date", target = "date")
    @Mapping(source = "workout.time", target = "time")
    ExerciseLogGraphDto mapToExerciseLogGraphDto(ExerciseLog exerciseLog);
}
