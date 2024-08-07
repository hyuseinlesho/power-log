package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.*;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.service.RoutineService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/workouts/routines")
public class RoutineController {
    private final RoutineService routineService;
    private final ExerciseService exerciseService;

    public RoutineController(RoutineService routineService, ExerciseService exerciseService) {
        this.routineService = routineService;
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public String showRoutinesList(Model model) {
        List<RoutineDto> routines = routineService.getAllRoutines();
        model.addAttribute("routines", routines);
        return "/routines/list";
    }

    @GetMapping("/create")
    public String showCreateRoutineForm(Model model) {
        model.addAttribute("routineDto", new RoutineDto());
        model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
        model.addAttribute("exerciseTypes", ExerciseType.values());
        return "/routines/create";
    }

    @PostMapping("/create")
    public String createRoutine(@RequestParam(required = false) Integer exerciseCount,
                                @Valid @ModelAttribute("routineDto") CreateRoutineDto routineDto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (exerciseCount != null) {
            List<RoutineExerciseDto> exercises = new ArrayList<>();
            for (int i = 0; i < exerciseCount; i++) {
                exercises.add(new RoutineExerciseDto());
            }
            routineDto.setExercises(exercises);
            model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
            return "/routines/create";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
            return "/routines/create";
        }

        routineService.createRoutine(routineDto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Routine created successfully!");
        return "redirect:/workouts/routines";
    }

    @GetMapping("/{id}/edit")
    public String showUpdateRoutineForm(@PathVariable("id") Long id,
                                        Model model) {
        RoutineDto routine = routineService.getRoutineById(id);
        model.addAttribute("routineDto", routine);
        model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
        model.addAttribute("exerciseTypes", ExerciseType.values());
        return "/routines/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateRoutine(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("routineDto") UpdateRoutineDto routineDto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        routineDto.setId(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("exerciseOptions", exerciseService.getAllExercises());
            return "/routines/edit";
        }

        routineService.updateRoutine(routineDto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Routine updated successfully!");
        return "redirect:/workouts/routines/{id}/details";
    }

    @GetMapping("/{id}/details")
    public String showRoutineDetails(@PathVariable Long id, Model model) {
        RoutineDto routine = routineService.getRoutineById(id);
        model.addAttribute("routine", routine);
        return "/routines/details";
    }

    @PostMapping("/{id}/delete")
    public String deleteWorkout(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        routineService.deleteRoutine(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Routine deleted successfully!");
        return "redirect:/workouts/routines";
    }
}
