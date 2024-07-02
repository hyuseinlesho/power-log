package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.exception.ExerciseAlreadyExistsException;
import com.hyuseinlesho.powerlog.mapper.ExerciseMapper;
import com.hyuseinlesho.powerlog.model.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateExerciseDto;
import com.hyuseinlesho.powerlog.model.entity.Exercise;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.repository.ExerciseRepository;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.security.SecurityUtil;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    public static final String ERROR_MESSAGE = "Exercise with the same name and type already exists.";

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Exercise createExercise(CreateExerciseDto exerciseDto) {
        Exercise exercise = ExerciseMapper.INSTANCE.mapToExercise(exerciseDto);

        String username = SecurityUtil.getSessionUser();
        List<Exercise> exercises = exerciseRepository.findAllByUserUsername(username);

        if (exercises.contains(exercise)) {
            throw new ExerciseAlreadyExistsException(ERROR_MESSAGE);
        }

        exercise.setUser(getUser());
        return exerciseRepository.save(exercise);
    }

    @Override
    public CreateExerciseDto findExerciseById(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).get();
        return ExerciseMapper.INSTANCE.mapToCreateExerciseDto(exercise);
    }

    @Override
    public List<ExerciseDto> findAllExercises() {
        String username = SecurityUtil.getSessionUser();

        List<Exercise> exercises = exerciseRepository.findAllByUserUsername(username);
        return exercises.stream()
                .map(ExerciseMapper.INSTANCE::mapToExerciseDto)
                .toList();
    }

    @Override
    public Exercise updateExercise(Long id, UpdateExerciseDto exerciseDto) {
        Exercise exercise = exerciseRepository.findById(id).get();
        exercise.setName(exerciseDto.getName());
        exercise.setType(exerciseDto.getType());

        String username = SecurityUtil.getSessionUser();

        List<Exercise> exercises = exerciseRepository.findAllByUserUsername(username);
        if (exercises.contains(exercise)) {
            throw new ExerciseAlreadyExistsException(ERROR_MESSAGE);
        }

        return exerciseRepository.save(exercise);
    }

    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }

    @Override
    public boolean addNewExercise(String name, ExerciseType type) {
        Optional<Exercise> optional = exerciseRepository.findByNameAndType(name, type);

        if (optional.isEmpty()) {
            Exercise exercise = new Exercise();
            exercise.setName(name);
            exercise.setType(type);
            exercise.setUser(getUser());

            exerciseRepository.save(exercise);
            return true;
        }

        return false;
    }

    private UserEntity getUser() {
        String username = SecurityUtil.getSessionUser();
        return userRepository.findByUsername(username);
    }
}
