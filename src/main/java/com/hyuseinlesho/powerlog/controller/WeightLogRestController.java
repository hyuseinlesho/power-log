package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.mapper.WeightLogMapper;
import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.UpdateWeightLogDto;
import com.hyuseinlesho.powerlog.model.dto.WeightLogResponseDto;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import com.hyuseinlesho.powerlog.service.WeightLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/weight-logs")
public class WeightLogRestController {
    private final WeightLogService weightLogService;

    public WeightLogRestController(WeightLogService weightLogService) {
        this.weightLogService = weightLogService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWeightLog(@Valid @RequestBody CreateWeightLogDto weightLogDto,
                                             BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        WeightLog created = weightLogService.createWeightLog(weightLogDto);
        WeightLogResponseDto response = WeightLogMapper.INSTANCE.mapToWeightLogResponseDto(created);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWeightLog(@PathVariable Long id,
                                             @Valid @RequestBody UpdateWeightLogDto weightLogDto,
                                             BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        WeightLog updated = weightLogService.updateWeightLog(id, weightLogDto);
        WeightLogResponseDto response = WeightLogMapper.INSTANCE.mapToWeightLogResponseDto(updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWeightLog(@PathVariable Long id) {
        weightLogService.deleteWeightLog(id);
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
