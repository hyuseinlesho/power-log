package com.hyuseinlesho.powerlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/demo/error/500")
    public String show500ErrorDemo() {
        return "admin/demo/errors/500";
    }

    @GetMapping("/demo/auth/login")
    public String showLoginDemo() {
        return "admin/demo/auth/login";
    }

    @GetMapping("/demo/auth/register")
    public String showRegisterDemo() {
        return "admin/demo/auth/register";
    }

    @GetMapping("/demo/layouts/static-nav")
    public String showStaticNavDemo() {
        return "admin/demo/layouts/static-nav";
    }

    @GetMapping("/demo/layouts/light-sidenav")
    public String showLightSidenavDemo() {
        return "admin/demo/layouts/light-sidenav";
    }

    @GetMapping("/info/charts")
    public String showChartsInfo() {
        return "admin/info/charts";
    }

    @GetMapping("/info/tables")
    public String showTablesInfo() {
        return "admin/info/tables";
    }
}
