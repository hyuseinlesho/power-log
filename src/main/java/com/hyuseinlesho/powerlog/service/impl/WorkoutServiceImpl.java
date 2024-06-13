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
    private final UserRepository userService;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, UserRepository userService) {
        this.workoutRepository = workoutRepository;
        this.userService = userService;
    }

    @Override
    public List<Workout> findWorkoutsByUsername(String username) {
        UserEntity user = userService.findByUsername(username);
        return workoutRepository.findByUser(user);
    }

    @Override
    public void createWorkout(WorkoutDto workoutDto, String username) {
        UserEntity user = userService.findByUsername(username);

        Workout workout = WorkoutMapper.INSTANCE.workoutDtoToWorkout(workoutDto);
        workout.setUser(user);

//        workoutRepository.save(workout);
    }
}
