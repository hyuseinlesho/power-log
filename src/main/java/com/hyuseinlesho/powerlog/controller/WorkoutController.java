package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.service.WorkoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class WorkoutController {
    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/workout-log")
    public String showWorkoutLog(Model model, Principal principal) {
        // TODO implement fetch users username when add Spring Security
        String username = "test_user";

        model.addAttribute("workouts", workoutService.findWorkoutsByUsername(username));
        return "workouts";
    }
}
