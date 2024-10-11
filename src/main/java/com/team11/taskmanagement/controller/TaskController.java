package com.team11.taskmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaskController {
    @GetMapping("/tasks")
    public String tasks(Model model) {
        model.addAttribute("currentUrl", "/tasks");
        return "tasks/index";
    }
}
