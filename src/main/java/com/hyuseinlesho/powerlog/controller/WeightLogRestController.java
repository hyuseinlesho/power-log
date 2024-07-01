package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.CreateWeightLogDto;
import com.hyuseinlesho.powerlog.model.entity.WeightLog;
import com.hyuseinlesho.powerlog.service.WeightLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        WeightLog created = weightLogService.createWeightLog(weightLogDto);
        return ResponseEntity.ok(created);
    }
}
