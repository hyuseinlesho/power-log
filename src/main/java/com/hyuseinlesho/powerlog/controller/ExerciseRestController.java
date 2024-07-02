package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.mapper.ExerciseMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.ExerciseResponseDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateExerciseDto;
import com.hyuseinlesho.powerlog.model.entity.Exercise;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseRestController {
    private final ExerciseService exerciseService;

    public ExerciseRestController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createExercise(@Valid @RequestBody CreateExerciseDto exerciseDto,
                                             BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        Exercise created = exerciseService.createExercise(exerciseDto);
        ExerciseResponseDto response = ExerciseMapper.INSTANCE.mapToExerciseResponseDto(created);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExercise(@PathVariable Long id,
                                            @Valid @RequestBody UpdateExerciseDto exerciseDto,
                                             BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        Exercise updated = exerciseService.updateExercise(id, exerciseDto);
        ExerciseResponseDto response = ExerciseMapper.INSTANCE.mapToExerciseResponseDto(updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
