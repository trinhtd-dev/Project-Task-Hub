package com.team11.taskmanagement.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.team11.taskmanagement.dto.ProjectDTO;
import com.team11.taskmanagement.model.Project;
import com.team11.taskmanagement.service.ProjectService;
import com.team11.taskmanagement.service.UserService;

@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/projects")
    public String projects(Model model) {
        model.addAttribute("currentUrl", "/projects");
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects/index";
    }

    @GetMapping("/projects/create")
    public String createProjectForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("project", new Project());
        return "projects/project-create";
    }


    @PostMapping("/projects/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO) {
        try {
            Project createdProject = projectService.createProject(projectDTO);
            return ResponseEntity.ok(createdProject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        } 
    }

    @GetMapping("/projects/trash")
    public String trash(Model model) {
        model.addAttribute("currentUrl", "/projects/trash");
        return "projects/trash";
    }
}
