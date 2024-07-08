package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.model.dto.ExerciseLogGraphDto;
import com.hyuseinlesho.powerlog.model.entity.ExerciseLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExerciseLogMapper {
    ExerciseLogMapper INSTANCE = Mappers.getMapper(ExerciseLogMapper.class);

    @Mapping(source = "workout.date", target = "date")
    @Mapping(source = "workout.time", target = "time")
    ExerciseLogGraphDto mapToExerciseLogGraphDto(ExerciseLog exerciseLog);
}
