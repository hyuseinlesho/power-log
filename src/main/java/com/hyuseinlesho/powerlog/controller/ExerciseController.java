package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.ExerciseDto;
import com.hyuseinlesho.powerlog.exception.ExerciseAlreadyExistsException;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @ModelAttribute("exerciseTypes")
    public ExerciseType[] exerciseTypes() {
        return ExerciseType.values();
    }

    @GetMapping()
    public String showExercisesPage(Model model) {
        model.addAttribute("exercises", exerciseService.findAllExercises());
        return "exercises";
    }

    @GetMapping("/create")
    public String showCreateExerciseForm(Model model) {
        model.addAttribute("exerciseDto", new ExerciseDto());
        return "exercises-create";
    }
    @PostMapping("/create")
    public String createExercise(@Valid @ModelAttribute("exerciseDto") ExerciseDto exerciseDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "exercises-create";
        }

        try {
            exerciseService.createExercise(exerciseDto);
            return "redirect:/exercises";
        } catch (ExerciseAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/exercises/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditExerciseForm(@PathVariable("id") Long id,
                                       Model  model) {
        ExerciseDto exerciseDto = exerciseService.findExerciseById(id);
        model.addAttribute("exerciseDto", exerciseDto);
        return "exercises-edit";
    }

    @PostMapping("/{id}/edit")
    public String editExercise(@PathVariable("id") Long id,
                               @Valid @ModelAttribute("exerciseDto") ExerciseDto exerciseDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "exercises-edit";
        }

        try {
            exerciseDto.setId(id);
            exerciseService.editExercise(exerciseDto);
            return "redirect:/exercises";
        } catch (ExerciseAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/exercises/{id}/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return "redirect:/exercises";
    }
}
