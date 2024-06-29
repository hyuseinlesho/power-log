package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.UserDto;
import com.hyuseinlesho.powerlog.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserDto user() {
        return userService.getSessionUser();
    }

    @GetMapping("/profile")
    public String showProfilePage() {
        return "/users/profile";
    }

    // TODO Fix edit fields with modals

    @PostMapping("/profile/change-username")
    @ResponseBody
    public ResponseEntity<?> changeUsername(@RequestBody Map<String, String> payload) {
        String newUsername = payload.get("username");
        boolean success = userService.changeUsername(newUsername);

        if (success) {
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Failed to change username"));
        }
    }

    @PostMapping("/profile/change-email")
    @ResponseBody
    public ResponseEntity<?> changeEmail(@RequestBody Map<String, String> payload) {
        String newEmail = payload.get("email");
        boolean success = userService.changeEmail(newEmail);

        if (success) {
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Failed to change email"));
        }
    }

    @PostMapping("/profile/change-password")
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload) {
        String newPassword = payload.get("password");
        boolean success = userService.changePassword(newPassword);

        if (success) {
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Failed to change password"));
        }
    }
}
