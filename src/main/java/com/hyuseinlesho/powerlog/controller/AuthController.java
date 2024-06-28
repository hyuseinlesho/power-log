package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.RegisterUserDto;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
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
    public RegisterUserDto registerDto() {
        return new RegisterUserDto();
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "/auth/register";
    }
    @PostMapping("/register")
    public String register(@Valid RegisterUserDto registerDto,
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

            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            return "redirect:/register?fail";
        }

        UserEntity existingUserEmail = userService.findByEmail(registerDto.getEmail());
        if (existingUserEmail != null
                && existingUserEmail.getEmail() != null
                && !existingUserEmail.getEmail().isEmpty()) {

            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            return "redirect:/register?fail";
        }

        userService.registerUser(registerDto);
        return "redirect:/login?success";
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "/auth/login";
    }
}
