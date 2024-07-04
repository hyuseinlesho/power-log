package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.exception.ExerciseAlreadyExistsException;
import com.hyuseinlesho.powerlog.mapper.ExerciseMapper;
import com.hyuseinlesho.powerlog.model.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateExerciseDto;
import com.hyuseinlesho.powerlog.model.entity.Exercise;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.repository.ExerciseRepository;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.security.SecurityUtil;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, UserService userService, UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public Exercise createExercise(CreateExerciseDto exerciseDto) {
        Exercise exercise = ExerciseMapper.INSTANCE.mapToExercise(exerciseDto);

        List<Exercise> exercises = exerciseRepository.findAllByUserUsername(SecurityUtil.getSessionUser());
        if (exercises.contains(exercise)) {
            throw new ExerciseAlreadyExistsException();
        }

        exercise.setUser(userService.getCurrentUser());
        return exerciseRepository.save(exercise);
    }

    @Override
    public boolean addNewExercise(String name, ExerciseType type) {
        Optional<Exercise> optional = exerciseRepository.findByNameAndType(name, type);

        if (optional.isEmpty()) {
            Exercise exercise = new Exercise();
            exercise.setName(name);
            exercise.setType(type);
            exercise.setUser(userService.getCurrentUser());
            exerciseRepository.save(exercise);
            return true;
        }
        return false;
    }

    @Override
    public CreateExerciseDto findExerciseById(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).get();
        return ExerciseMapper.INSTANCE.mapToCreateExerciseDto(exercise);
    }

    @Override
    public List<ExerciseDto> findAllExercises() {
        List<Exercise> exercises = exerciseRepository.findAllByUserUsername(SecurityUtil.getSessionUser());
        return exercises.stream()
                .map(ExerciseMapper.INSTANCE::mapToExerciseDto)
                .toList();
    }

    @Override
    public Exercise updateExercise(Long id, UpdateExerciseDto exerciseDto) {
        Exercise exercise = exerciseRepository.findById(id).get();
        exercise.setName(exerciseDto.getName());
        exercise.setType(exerciseDto.getType());

        List<Exercise> exercises = exerciseRepository.findAllByUserUsername(SecurityUtil.getSessionUser());
        if (exercises.contains(exercise)) {
            throw new ExerciseAlreadyExistsException();
        }

        return exerciseRepository.save(exercise);
    }

    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }
}
