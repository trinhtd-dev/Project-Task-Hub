package com.team11.taskmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.team11.taskmanagement.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("currentUrl", "/users");
        model.addAttribute("users", userService.getAllUsers());
        return "users/index";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("currentUrl", "/profile");
        return "users/profile";
    }
}
