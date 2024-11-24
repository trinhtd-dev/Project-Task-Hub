package com.team11.taskmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.transaction.annotation.Transactional;

import com.team11.taskmanagement.dto.user.UserCreateDTO;
import com.team11.taskmanagement.service.UserService;
import com.team11.taskmanagement.service.ProjectService;
import com.team11.taskmanagement.model.User;
import com.team11.taskmanagement.service.TaskService;
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

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

    @GetMapping("/users/profile")
    @Transactional(readOnly = true)
    public String profile(Model model) {
        model.addAttribute("currentUrl", "/users/profile");
        User user = userService.getCurrentUser();
        model.addAttribute("projects", projectService.getProjectsByUserId(user.getId()));
        model.addAttribute("tasks", taskService.getTasksOfUser());
        return "users/profile";
    }
}
