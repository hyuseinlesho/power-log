package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.service.WorkoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {
    // TODO Implement fetch username from currently logged-in user.
    public static final String TEST_USER = "john_doe";
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;

    public WorkoutController(WorkoutService workoutService, ExerciseService exerciseService) {
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/history")
    public String showWorkoutHistory(Model model) {
        List<Workout> workouts = workoutService.findWorkoutsByUsername(TEST_USER);
        model.addAttribute("workouts", workouts);
        return "workouts-history";
    }

    @GetMapping("/create")
    public String showCreateWorkoutForm(Model model) {
        model.addAttribute("workout", new WorkoutDto());
        model.addAttribute("exercises", exerciseService.findAllExercisesForUser(TEST_USER));
        return "workouts-create";
    }

    @PostMapping("/create")
    public String createWorkout(WorkoutDto workoutDto) {
        workoutService.createWorkout(workoutDto, TEST_USER);
        return "redirect:/workouts/history";
    }

    @GetMapping("/{workoutId}/edit")
    public String showEditWorkoutForm(@PathVariable("workoutId") Long workoutId, Model model) {
        WorkoutDto workoutDto = workoutService.findWorkoutById(workoutId);
        model.addAttribute("workout", workoutDto);
        model.addAttribute("exercises", exerciseService.findAllExercisesForUser(TEST_USER));
        return "workouts-edit";
    }

    @PostMapping("/{workoutId}/edit")
    public String editWorkout(@PathVariable("workoutId") Long workoutId, WorkoutDto workoutDto) {
        workoutDto.setId(workoutId);
        workoutService.editWorkout(workoutDto, TEST_USER);
        return "redirect:/workouts/history";
    }
}
