package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {
    public static final String TEST_USER = "test_user";
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping()
    public String showExercisesPage(Model model) {
        model.addAttribute("exercises", exerciseService.findAllExercisesForUser(TEST_USER));
        return "exercises";
    }

    @GetMapping("/create")
    public String showCreateExerciseForm(Model model) {
        model.addAttribute("exercise", new ExerciseDto());
        model.addAttribute("exerciseTypes", ExerciseType.values());
        return "exercises-create";
    }
    @PostMapping("/create")
    public String createExercise(ExerciseDto exerciseDto, Principal principal) {
        // Test user

        // Way with principal
//        workoutService.createWorkout(workoutDto, principal.getName());
        exerciseService.createExercise(exerciseDto, TEST_USER);
        return "redirect:/exercises";
    }
}
