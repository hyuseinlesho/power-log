package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.ChangeEmailDto;
import com.hyuseinlesho.powerlog.model.dto.ChangePasswordDto;
import com.hyuseinlesho.powerlog.model.dto.UserProfileDto;
import com.hyuseinlesho.powerlog.service.UserService;
import com.hyuseinlesho.powerlog.util.ControllerUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserProfileDto user() {
        return userService.getSessionUser();
    }

    @GetMapping("/profile")
    public String showProfilePage() {
        return "/users/profile";
    }

    @PostMapping("/profile/change-email")
    @ResponseBody
    public ResponseEntity<?> changeEmail(@Valid @RequestBody ChangeEmailDto emailDto,
                                         BindingResult bindingResult) {
        ResponseEntity<Map<String, Object>> errorResponse = ControllerUtil.handleValidationErrors(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        Map<String, Object> response = new HashMap<>();
        boolean success = userService.changeEmail(emailDto.getEmail());
        response.put("success", success);
        response.put("message", success ? "Email changed successfully." : "Failed to change email.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile/change-password")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changePassword(@Valid @RequestBody ChangePasswordDto passwordDto,
                                                              BindingResult bindingResult) {
        ResponseEntity<Map<String, Object>> errorResponse = ControllerUtil.handleValidationErrors(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        Map<String, Object> response = new HashMap<>();
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            response.put("success", false);
            response.put("message", "New password and confirmation do not match");
            return ResponseEntity.badRequest().body(response);
        }

        boolean success = userService.changePassword(passwordDto.getOldPassword(), passwordDto.getNewPassword());
        response.put("success", success);
        response.put("message", success ? "Password changed successfully." : "Old password isn't correct");
        return ResponseEntity.ok(response);
    }
}
