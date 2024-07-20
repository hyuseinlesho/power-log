package com.hyuseinlesho.powerlog.service;

import com.hyuseinlesho.powerlog.model.dto.CreateRoutineDto;
import com.hyuseinlesho.powerlog.model.dto.RoutineDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateRoutineDto;
import com.hyuseinlesho.powerlog.model.entity.Routine;

import java.util.List;

public interface RoutineService {
    void createRoutine(CreateRoutineDto routineDto);

    List<RoutineDto> getRoutines();

    RoutineDto getRoutineById(Long id);

    void editRoutine(UpdateRoutineDto routineDto);

    void deleteWorkout(Long id);
}
