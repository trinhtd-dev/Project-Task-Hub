package com.team11.taskmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.team11.taskmanagement.service.UserService;
import com.team11.taskmanagement.dto.user.UserCreateDTO;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute UserCreateDTO userRequestDTO, Model model) {
        userService.createUser(userRequestDTO);
        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("currentUrl", "/profile");
        return "users/profile";
    }
}
