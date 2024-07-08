package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping()
    public String showExercisesPage(Model model) {
        model.addAttribute("exercises", exerciseService.findAllExercises());
        model.addAttribute("exerciseTypes", ExerciseType.values());
        return "/exercises/list";
    }

    @GetMapping("/graph")
    public String showExercisesGraph(Model model) {
        model.addAttribute("exerciseOptions", exerciseService.findAllExercises());
        return "/exercises/graph";
    }
}
