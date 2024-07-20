package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.repository.RoutineExerciseRepository;
import com.hyuseinlesho.powerlog.service.RoutineExerciseService;
import org.springframework.stereotype.Service;

@Service
public class RoutineExerciseServiceImpl implements RoutineExerciseService {
    private final RoutineExerciseRepository routineExerciseRepository;

    public RoutineExerciseServiceImpl(RoutineExerciseRepository routineExerciseRepository) {
        this.routineExerciseRepository = routineExerciseRepository;
    }
}
