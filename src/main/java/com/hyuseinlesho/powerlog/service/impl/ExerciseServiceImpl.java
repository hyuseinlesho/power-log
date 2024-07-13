package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.exception.ExerciseAlreadyExistsException;
import com.hyuseinlesho.powerlog.exception.ExerciseNotFoundException;
import com.hyuseinlesho.powerlog.mapper.ExerciseMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateExerciseDto;
import com.hyuseinlesho.powerlog.model.entity.Exercise;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.repository.ExerciseRepository;
import com.hyuseinlesho.powerlog.security.SecurityUtil;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final UserService userService;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper, UserService userService) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
        this.userService = userService;
    }

    @Override
    public Exercise createExercise(CreateExerciseDto exerciseDto) {
        Exercise exercise = exerciseMapper.mapToExercise(exerciseDto);

        List<Exercise> exercises = exerciseRepository.findAllByUser(userService.getCurrentUser());
        if (exercises.contains(exercise)) {
            throw new ExerciseAlreadyExistsException();
        }

        exercise.setUser(userService.getCurrentUser());
        return exerciseRepository.save(exercise);
    }

    @Override
    public boolean addNewExercise(String name, ExerciseType type) {
        Optional<Exercise> optional = exerciseRepository.findByNameAndTypeAndUser(
                name, type, userService.getCurrentUser()
        );

        if (optional.isEmpty()) {
            Exercise exercise = Exercise.builder()
                    .name(name)
                    .type(type)
                    .user(userService.getCurrentUser()).build();

            exerciseRepository.save(exercise);
            return true;
        }

        return false;
    }

    @Override
    public ExerciseDto findExerciseById(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found for id: " + id));;
        return exerciseMapper.mapToExerciseDto(exercise);
    }

    @Override
    public List<ExerciseDto> findAllExercises() {
        List<Exercise> exercises = exerciseRepository.findAllByUser(userService.getCurrentUser());
        return exercises.stream()
                .map(ExerciseMapper.INSTANCE::mapToExerciseDto)
                .toList();
    }

    @Override
    public Exercise updateExercise(Long id, UpdateExerciseDto exerciseDto) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found for id: " + id));;
        exercise.setName(exerciseDto.getName());
        exercise.setType(exerciseDto.getType());

        List<Exercise> exercises = exerciseRepository.findAllByUser(userService.getCurrentUser());
        if (exercises.contains(exercise)) {
            throw new ExerciseAlreadyExistsException();
        }

        return exerciseRepository.save(exercise);
    }

    @Override
    public void deleteExercise(Long id) {
        if (!exerciseRepository.existsById(id)) {
            throw new ExerciseNotFoundException("Exercise not found for id: " + id);
        }

        exerciseRepository.deleteById(id);
    }
}
