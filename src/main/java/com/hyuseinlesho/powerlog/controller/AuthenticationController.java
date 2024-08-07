package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.model.dto.LoginUserDto;
import com.hyuseinlesho.powerlog.model.dto.RegisterUserDto;
import com.hyuseinlesho.powerlog.security.jwt.refreshtoken.RefreshToken;
import com.hyuseinlesho.powerlog.model.entity.UserEntity;
import com.hyuseinlesho.powerlog.security.jwt.JwtProperties;
import com.hyuseinlesho.powerlog.security.jwt.JwtService;
import com.hyuseinlesho.powerlog.security.jwt.refreshtoken.RefreshTokenService;
import com.hyuseinlesho.powerlog.service.AuthenticationService;
import com.hyuseinlesho.powerlog.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(
            AuthenticationService authenticationService,
            JwtService jwtService,
            JwtProperties jwtProperties,
            RefreshTokenService refreshTokenService
    ) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.refreshTokenService = refreshTokenService;
    }


    @ModelAttribute("registerDto")
    public RegisterUserDto registerDto() {
        return new RegisterUserDto();
    }

    @ModelAttribute("loginDto")
    public LoginUserDto loginDto() {
        return new LoginUserDto();
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
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.registerDto",
                    bindingResult);
            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            return "redirect:/auth/register";
        }

        UserEntity existingUserUsername = authenticationService.getByUsername(registerDto.getUsername());
        if (existingUserUsername != null) {
            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            return "redirect:/auth/register?fail";
        }

        UserEntity existingUserEmail = authenticationService.getByEmail(registerDto.getEmail());
        if (existingUserEmail != null) {
            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            return "redirect:/auth/register?fail";
        }

        authenticationService.register(registerDto);
        return "redirect:/auth/login?success";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "/auth/login";
    }

    @PostMapping("/login")
    public String authenticate(@Valid LoginUserDto loginDto,
                               BindingResult bindingResult,
                               @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
                               RedirectAttributes redirectAttributes,
                               HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.loginDto",
                    bindingResult);
            redirectAttributes.addFlashAttribute("loginDto", loginDto);
            return "redirect:/auth/login";
        }

        try {
            UserEntity authenticatedUser = authenticationService.authenticate(loginDto);

            // For development purpose
            refreshTokenService.deleteByUser(authenticatedUser);

            String jwtToken = jwtService.generateToken(authenticatedUser);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginDto.getUsername(), rememberMe);

            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", jwtToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(jwtProperties.getAccessTokenCookieMaxAge()).build();

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(rememberMe
                            ? jwtProperties.getRememberMeRefreshTokenCookieMaxAge()
                            : jwtProperties.getRefreshTokenCookieMaxAge()).build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            return "redirect:/home?success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("loginDto", loginDto);
            return "redirect:/auth/login?error";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        CookieUtil.invalidateCookie(response, "accessToken");
        CookieUtil.invalidateCookie(response, "refreshToken");

        String refreshToken = getCookieValue(request);
        if (refreshToken != null) {
            refreshTokenService.findByToken(refreshToken).ifPresent(refreshTokenService::deleteToken);
        }

        SecurityContextHolder.clearContext();

        return "redirect:/auth/login?logout";
    }

    private String getCookieValue(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
