package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.dto.UserRegisterDto;
import com.hyuseinlesho.powerlog.model.UserEntity;
import com.hyuseinlesho.powerlog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("registerDto")
    public UserRegisterDto registerDto() {
        return new UserRegisterDto();
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }
    @PostMapping("/register")
    public String register(@Valid UserRegisterDto registerDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDto", bindingResult);
            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            return "redirect:/register";
        }

        UserEntity existingUserUsername = userService.findByUsername(registerDto.getUsername());
        if (existingUserUsername != null
                && existingUserUsername.getUsername() != null
                && !existingUserUsername.getUsername().isEmpty()) {

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Username is already in use.");
            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            return "redirect:/register";
        }

        UserEntity existingUserEmail = userService.findByEmail(registerDto.getEmail());
        if (existingUserEmail != null
                && existingUserEmail.getEmail() != null
                && !existingUserEmail.getEmail().isEmpty()) {

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Email is already in use.");
            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            return "redirect:/register";
        }

        userService.registerUser(registerDto);

        // TODO Implement home page
        return "redirect:/";
    }
}
