package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.service.WorkoutService;
import org.springframework.stereotype.Controller;

@Controller
public class WorkoutController {
    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }


}
