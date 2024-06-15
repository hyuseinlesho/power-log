package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {
    // TODO Implement fetch username from currently logged-in user.
    public static final String TEST_USER = "john_doe";
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
    public String createExercise(ExerciseDto exerciseDto) {
        exerciseService.createExercise(exerciseDto, TEST_USER);
        return "redirect:/exercises";
    }

    @GetMapping("/{exerciseId}/edit")
    public String showEditExerciseForm(@PathVariable("exerciseId") Long exerciseId, Model  model) {
        ExerciseDto exerciseDto = exerciseService.findExerciseById(exerciseId);
        model.addAttribute("exercise", exerciseDto);
        model.addAttribute("exerciseTypes", ExerciseType.values());
        return "exercises-edit";
    }

    @PostMapping("/{exerciseId}/edit")
    public String editExercise(@PathVariable("exerciseId") Long exerciseId, ExerciseDto exerciseDto) {
        exerciseDto.setId(exerciseId);
        exerciseService.editExercise(exerciseDto, TEST_USER);
        return "redirect:/exercises";
    }
}
