package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.exception.RoutineNotFoundException;
import com.hyuseinlesho.powerlog.mapper.RoutineMapper;
import com.hyuseinlesho.powerlog.model.dto.*;
import com.hyuseinlesho.powerlog.model.entity.Routine;
import com.hyuseinlesho.powerlog.model.entity.RoutineExercise;
import com.hyuseinlesho.powerlog.repository.RoutineExerciseRepository;
import com.hyuseinlesho.powerlog.repository.RoutineRepository;
import com.hyuseinlesho.powerlog.service.RoutineService;
import com.hyuseinlesho.powerlog.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutineServiceImpl implements RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;
    private final RoutineExerciseRepository routineExerciseRepository;
    private final UserService userService;

    public RoutineServiceImpl(
            RoutineRepository routineRepository,
            RoutineMapper routineMapper,
            RoutineExerciseRepository routineExerciseRepository,
            UserService userService
    ) {
        this.routineRepository = routineRepository;
        this.routineMapper = routineMapper;
        this.routineExerciseRepository = routineExerciseRepository;
        this.userService = userService;
    }

    @Override
    public void createRoutine(CreateRoutineDto routineDto) {
        Routine routine = routineMapper.mapToRoutine(routineDto);
        routine.setUser(userService.getCurrentUser());

        List<RoutineExercise> exercises = routine.getExercises();
        for (RoutineExercise exercise : exercises) {
            exercise.setRoutine(routine);
        }

        routineRepository.save(routine);
    }

    @Override
    public List<RoutineDto> getAllRoutines() {
        List<Routine> routines = routineRepository.findAllByUser(userService.getCurrentUser());
        return routines.stream()
                .map(routineMapper::mapToRoutineDto)
                .toList();
    }

    @Override
    public RoutineDto getRoutineById(Long id) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new RoutineNotFoundException("Routine not found for id: " + id));
        ;
        return routineMapper.mapToRoutineDto(routine);
    }

    @Override
    public void updateRoutine(UpdateRoutineDto routineDto) {
        Routine routine = routineRepository.findById(routineDto.getId())
                .orElseThrow(() -> new RoutineNotFoundException("Routine not found for id: " + routineDto.getId()));
        routine.setName(routineDto.getName());

        List<RoutineExercise> exercises = routineExerciseRepository.findAllByRoutine(routine);
        List<RoutineExerciseDto> exerciseDtos = routineDto.getExercises();

        for (int i = 0; i < exercises.size(); i++) {
            RoutineExercise exercise = exercises.get(i);
            RoutineExerciseDto exerciseDto = exerciseDtos.get(i);

            exercise.setName(exerciseDto.getName());
            exercise.setSets(exerciseDto.getSets());
            exercise.setReps(exerciseDto.getReps());
        }

        routine.setExercises(exercises);

        routineRepository.save(routine);
    }

    @Override
    public void deleteRoutine(Long id) {
        if (!routineRepository.existsById(id)) {
            throw new RoutineNotFoundException("Routine not found for id: " + id);
        }

        routineRepository.deleteById(id);
    }
}
