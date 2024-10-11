package com.team11.taskmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.team11.taskmanagement.service.ProjectService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.team11.taskmanagement.model.Project;
@Controller
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public String projects(Model model) {
        model.addAttribute("currentUrl", "/projects");
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects/index";
    }

    @GetMapping("/projects/create")
    public String createProjectForm(Model model) {
        return "projects/project-create";
    }

    @PostMapping("/projects/create")
    public String createProject(@ModelAttribute Project project) {
        projectService.createProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/projects/trash")
    public String trash(Model model) {
        model.addAttribute("currentUrl", "/projects/trash");
        return "projects/trash";
    }
}
