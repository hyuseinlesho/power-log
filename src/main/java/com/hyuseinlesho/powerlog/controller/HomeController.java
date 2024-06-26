package com.hyuseinlesho.powerlog.controller;

import com.hyuseinlesho.powerlog.security.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String showHomePage(Model model) {
        model.addAttribute("user", SecurityUtil.getSessionUser());
        return "home";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }
}
