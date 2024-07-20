package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.model.dto.CreateRoutineDto;
import com.hyuseinlesho.powerlog.model.dto.RoutineDto;
import com.hyuseinlesho.powerlog.model.entity.Routine;
import org.mapstruct.Mapper;

@Mapper
public interface RoutineMapper {
    Routine mapToRoutine(CreateRoutineDto routineDto);

    RoutineDto mapToRoutineDto(Routine routine);
}
