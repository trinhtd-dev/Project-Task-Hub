package com.team11.taskmanagement.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.team11.taskmanagement.dto.task.TaskResponseDTO;
import com.team11.taskmanagement.service.TaskService;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        model.addAttribute("currentUrl", "/tasks");
        List<TaskResponseDTO> tasks = taskService.getTasksOfUser();
        model.addAttribute("tasks", tasks);
        return "tasks/index";
    }
}
