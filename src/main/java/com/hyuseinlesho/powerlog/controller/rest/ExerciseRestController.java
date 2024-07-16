package com.hyuseinlesho.powerlog.controller.rest;

import com.hyuseinlesho.powerlog.exception.ExerciseAlreadyExistsException;
import com.hyuseinlesho.powerlog.exception.ExerciseNotFoundException;
import com.hyuseinlesho.powerlog.mapper.ExerciseMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateExerciseDto;
import com.hyuseinlesho.powerlog.model.dto.ExerciseResponseDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateExerciseDto;
import com.hyuseinlesho.powerlog.model.entity.Exercise;
import com.hyuseinlesho.powerlog.service.ExerciseService;
import com.hyuseinlesho.powerlog.util.ControllerUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseRestController {
    private final ExerciseService exerciseService;
    private final ExerciseMapper exerciseMapper;

    public ExerciseRestController(ExerciseService exerciseService, ExerciseMapper exerciseMapper) {
        this.exerciseService = exerciseService;
        this.exerciseMapper = exerciseMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createExercise(@Valid @RequestBody CreateExerciseDto exerciseDto,
                                            BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = ControllerUtil.validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        try {
            Exercise created = exerciseService.createExercise(exerciseDto);
            ExerciseResponseDto response = exerciseMapper.mapToExerciseResponseDto(created);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ExerciseAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExercise(@PathVariable Long id,
                                            @Valid @RequestBody UpdateExerciseDto exerciseDto,
                                            BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = ControllerUtil.validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        try {
            Exercise updated = exerciseService.updateExercise(id, exerciseDto);
            ExerciseResponseDto response = exerciseMapper.mapToExerciseResponseDto(updated);
            return ResponseEntity.ok(response);
        } catch (ExerciseNotFoundException e) {
            return new ResponseEntity<>("Exercise not found", HttpStatus.NOT_FOUND);
        } catch (ExerciseAlreadyExistsException e) {
            return new ResponseEntity<>("Exercise already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
