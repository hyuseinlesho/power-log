package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.ExerciseLogDto;
import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;
import com.hyuseinlesho.powerlog.service.ExerciseLogService;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.service.WorkoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {
    public static final String TEST_USER = "john_doe";
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;

    public WorkoutController(WorkoutService workoutService, ExerciseService exerciseService) {
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/history")
    public String showWorkoutHistory(Model model) {
        // TODO Implement fetch username from currently logged-in user.

        List<Workout> workouts = workoutService.findWorkoutsByUsername(TEST_USER);
        model.addAttribute("workouts", workouts);
        return "workouts-history";
    }

    @GetMapping("/create")
    public String showCreateWorkoutForm(Model model) {
        WorkoutDto workoutDto = new WorkoutDto();
        List<ExerciseLogDto> exercises = new ArrayList<>();
        exercises.add(new ExerciseLogDto());
        workoutDto.setExercises(exercises);

        model.addAttribute("workout", workoutDto);
        model.addAttribute("exercises", exerciseService.findAllExercisesForUser(TEST_USER));
        return "workouts-create";
    }

    @PostMapping("/create")
    public String createWorkout(WorkoutDto workoutDto, Model model, Principal principal) {
        // Way with principal
//        workoutService.createWorkout(workoutDto, principal.getName());

        workoutService.createWorkout(workoutDto, TEST_USER);

        return "redirect:/workouts/history";
    }
}
