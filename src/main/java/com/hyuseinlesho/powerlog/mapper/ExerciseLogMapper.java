package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.dto.ExerciseLogDto;
import com.hyuseinlesho.powerlog.model.ExerciseLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExerciseLogMapper {
    ExerciseLogMapper INSTANCE = Mappers.getMapper(ExerciseLogMapper.class);
    
    ExerciseLog exerciseLogDtoToExerciseLog(ExerciseLogDto exerciseLogDto);
}
