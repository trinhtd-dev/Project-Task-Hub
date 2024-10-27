package com.team11.taskmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home/index";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
        if (logout != null)
            model.addAttribute("message", "Bạn đã đăng xuất thành công.");
        return "users/login";
    }

    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            model.addAttribute("currentUrl", "/dashboard");
            return "home/index";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/error")
    public String handleError() {
        return "error";
    }
}
