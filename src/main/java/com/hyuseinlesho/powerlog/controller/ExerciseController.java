package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.AddNewExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.ChangeEmailDto;
import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.exception.ExerciseAlreadyExistsException;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.util.ControllerUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

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
        return "/exercises/list";
    }

    @GetMapping("/create")
    public String showCreateExerciseForm(Model model) {
        model.addAttribute("exerciseDto", new CreateExerciseDto());
        return "/exercises/create";
    }
    @PostMapping("/create")
    public String createExercise(@Valid @ModelAttribute("exerciseDto") CreateExerciseDto exerciseDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/exercises/create";
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
        CreateExerciseDto exerciseDto = exerciseService.findExerciseById(id);
        model.addAttribute("exerciseDto", exerciseDto);
        return "/exercises/edit";
    }

    @PostMapping("/{id}/edit")
    public String editExercise(@PathVariable("id") Long id,
                               @Valid @ModelAttribute("exerciseDto") CreateExerciseDto exerciseDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/exercises/edit";
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

    @PostMapping("/add-new")
    @ResponseBody
    public ResponseEntity<?> changeEmail(@Valid @RequestBody AddNewExerciseDto exerciseDto,
                                         BindingResult bindingResult) {
        ResponseEntity<Map<String, Object>> errorResponse = ControllerUtil.handleValidationErrors(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        Map<String, Object> response = new HashMap<>();
        boolean success = exerciseService.addNewExercise(exerciseDto.getName(), exerciseDto.getType());
        response.put("success", success);
        response.put("message", success ? "Exercise added successfully." : "Exercise with the same name and type already exists.");
        return ResponseEntity.ok(response);
    }
}
