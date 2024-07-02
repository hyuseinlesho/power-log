package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.AddNewExerciseDto;
import com.hyuseinlesho.powerlog.model.enums.ExerciseType;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.util.ControllerUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        return "/exercises/new-list";
    }
    @PostMapping("/add-new")
    @ResponseBody
    public ResponseEntity<?> addNewExercise(@Valid @RequestBody AddNewExerciseDto exerciseDto,
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
