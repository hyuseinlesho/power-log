package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.service.WorkoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;

    public WorkoutController(WorkoutService workoutService, ExerciseService exerciseService) {
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/history")
    public String showWorkoutHistory(Model model) {
        // TODO Implement fetch username from currently logged-in user.
        String username = "test_user";

        List<Workout> workouts = workoutService.findWorkoutsByUsername(username);
        model.addAttribute("workouts", workouts);
        return "workouts-history";
    }

    @GetMapping("/create")
    public String showCreateWorkoutForm(Model model) {
        model.addAttribute("workoutDto", new WorkoutDto());
        model.addAttribute("exercises", exerciseService.findAllExercises());
        return "workouts-create";
    }

    @PostMapping("/create")
    public String createWorkout(@ModelAttribute("workoutDto") WorkoutDto workoutDto, Principal principal) {
        // Test user
        String username = "test_user";

        // Way with principal
//        workoutService.createWorkout(workoutDto, principal.getName());

        workoutService.createWorkout(workoutDto, username);
        return "redirect:/workouts/history";
    }
}
