package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.mapper.ExerciseMapper;
import com.hyuseinlesho.powerlog.model.Exercise;
import com.hyuseinlesho.powerlog.model.UserEntity;
import com.hyuseinlesho.powerlog.repository.ExerciseRepository;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Exercise> findAllExercises() {
        return exerciseRepository.findAll();
    }

    @Override
    public void createExercise(ExerciseDto exerciseDto, String username) {
        Exercise exercise = ExerciseMapper.INSTANCE.exerciseDtoToExercise(exerciseDto);

        List<Exercise> exercises = exerciseRepository.findAllByUserUsername(username);
        if (exercises.contains(exercise)) {
            throw new IllegalArgumentException("Exercise with the same name and type already exists for the user.");
        }
        exercise.setUser(getUser(username));
        
        exerciseRepository.save(exercise);
    }

    @Override
    public List<Exercise> findAllExercisesForUser(String username) {
        // TODO Implement it to return List<ExerciseDto>
        return exerciseRepository.findAllByUserUsername(username);
    }

    @Override
    public ExerciseDto findExerciseById(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).get();
        return ExerciseMapper.INSTANCE.exerciseToExerciseDto(exercise);
    }

    @Override
    public void editExercise(ExerciseDto exerciseDto, String username) {
        Exercise exercise = exerciseRepository.findById(exerciseDto.getId()).get();
        exercise.setName(exerciseDto.getName());
        exercise.setType(exerciseDto.getType());
        exerciseRepository.save(exercise);
    }

    private UserEntity getUser(String username) {
        return userRepository.findByUsername(username);
    }
}
