package com.hyuseinlesho.powerlog.service.impl;

import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.mapper.WorkoutMapper;
import com.hyuseinlesho.powerlog.model.UserEntity;
import com.hyuseinlesho.powerlog.model.Workout;
import com.hyuseinlesho.powerlog.repository.UserRepository;
import com.hyuseinlesho.powerlog.repository.WorkoutRepository;
import com.hyuseinlesho.powerlog.service.WorkoutService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Workout> findWorkoutsByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        return workoutRepository.findByUser(user);
    }

    @Override
    public void createWorkout(WorkoutDto workoutDto) {
        Workout workout = WorkoutMapper.INSTANCE.workoutDtoToWorkout(workoutDto);

        workoutRepository.save(workout);
    }
}
