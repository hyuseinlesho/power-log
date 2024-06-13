package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.service.WorkoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {
    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/history")
    public String showWorkoutHistory(Model model, Principal principal) {
        // TODO implement fetch user when add Spring Security
        String username = "test_user";

        model.addAttribute("workouts", workoutService.findWorkoutsByUsername(username));
        return "workouts-history";
    }

    @GetMapping("/create")
    public String showCreateWorkoutForm(Model model) {
        model.addAttribute("workoutDto", new WorkoutDto());
        return "workouts-create";
    }

    @PostMapping("/create")
    public String createWorkout(@ModelAttribute("workoutDto") WorkoutDto workoutDto) {
        workoutService.createWorkout(workoutDto);
        return "redirect:/workouts/history";
    }
}
