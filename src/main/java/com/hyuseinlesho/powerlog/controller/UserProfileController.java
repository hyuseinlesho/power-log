package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.ChangeEmailDto;
import com.hyuseinlesho.powerlog.model.dto.ChangePasswordDto;
import com.hyuseinlesho.powerlog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("users/profile")
public class UserProfileController {
    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/change-email")
    public ResponseEntity<?> changeEmail(@Valid @RequestBody ChangeEmailDto emailDto,
                                         BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        Map<String, Object> response = new HashMap<>();
        boolean success = userService.changeEmail(emailDto.getNewEmail());
        response.put("success", success);
        response.put("message", success ? "Email changed successfully." : "Failed to change email.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto passwordDto,
                                            BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = validateRequest(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        Map<String, Object> response = new HashMap<>();
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            response.put("success", false);
            response.put("message", "New password and confirmation do not match");
            return ResponseEntity.ok(response);
        }

        boolean success = userService.changePassword(passwordDto.getOldPassword(), passwordDto.getNewPassword());
        response.put("success", success);
        response.put("message", success ? "Password changed successfully." : "Old password isn't correct");
        return ResponseEntity.ok(response);
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
