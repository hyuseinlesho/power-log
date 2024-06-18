package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.ExerciseLogDto;
import com.hyuseinlesho.powerlog.dto.WorkoutDto;
import com.hyuseinlesho.powerlog.model.Workout;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {
    // TODO Implement fetch username from currently logged-in user.

    // TODO Fix date from Create and Edit Workout

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
        model.addAttribute("workoutDto", new WorkoutDto());
        model.addAttribute("exerciseOptions", exerciseService.findAllExercisesForUser(TEST_USER));
        return "workouts-create";
    }

    @PostMapping("/create")
    public String createWorkout(@RequestParam(required = false) Integer exerciseCount,
                                @Valid WorkoutDto workoutDto,
                                BindingResult bindingResult,
                                Model model) {
        if (exerciseCount != null) {
            List<ExerciseLogDto> exercises = new ArrayList<>();
            for (int i = 0; i < exerciseCount; i++) {
                exercises.add(new ExerciseLogDto());
            }
            workoutDto.setExercises(exercises);
            model.addAttribute("workoutDto", workoutDto);
            model.addAttribute("exerciseOptions", exerciseService.findAllExercisesForUser(TEST_USER));
            return "workouts-create";
        } else {
            if (bindingResult.hasErrors()) {
                model.addAttribute("workoutDto", workoutDto);
                model.addAttribute("exerciseOptions", exerciseService.findAllExercisesForUser(TEST_USER));
                return "workouts-create";
            }
            workoutService.createWorkout(workoutDto, TEST_USER);
            return "redirect:/workouts/history";
        }
    }

    @GetMapping("/{workoutId}/edit")
    public String showEditWorkoutForm(@PathVariable("workoutId") Long workoutId, Model model) {
        WorkoutDto workoutDto = workoutService.findWorkoutById(workoutId);
        model.addAttribute("workoutDto", workoutDto);
        model.addAttribute("exerciseOptions", exerciseService.findAllExercisesForUser(TEST_USER));
        return "workouts-edit";
    }

    @PostMapping("/{workoutId}/edit")
    public String editWorkout(@PathVariable("workoutId") Long workoutId,
                              @Valid WorkoutDto workoutDto,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            workoutDto.setId(workoutId);
            model.addAttribute("workoutDto", workoutDto);
            model.addAttribute("exerciseOptions", exerciseService.findAllExercisesForUser(TEST_USER));
            return "workouts-edit";
        }
        workoutDto.setId(workoutId);
        workoutService.editWorkout(workoutDto, TEST_USER);
        return "redirect:/workouts/history";
    }

    @GetMapping("/{id}/details")
    public String getWorkoutDetails(@PathVariable Long id,
                                    Model model) {
        WorkoutDto workoutDto = workoutService.findWorkoutById(id);
        model.addAttribute("workoutDto", workoutDto);
        return "workout-details";
    }
}
