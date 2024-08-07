package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateRoutineDto;
import com.hyuseinlesho.powerlog.model.dto.RoutineDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateRoutineDto;

import java.util.List;

public interface RoutineService {
    void createRoutine(CreateRoutineDto routineDto);

    List<RoutineDto> getAllRoutines();

    RoutineDto getRoutineById(Long id);

    void updateRoutine(UpdateRoutineDto routineDto);

    void deleteRoutine(Long id);
}
