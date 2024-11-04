package com.team11.taskmanagement.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team11.taskmanagement.dto.task.TaskCreateDTO;
import com.team11.taskmanagement.dto.task.TaskResponseDTO;
import com.team11.taskmanagement.service.TaskService;
import com.team11.taskmanagement.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectApiController {

    private final TaskService taskService;
    private final ProjectService projectService;

    public ProjectApiController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;   
        this.projectService = projectService;
    }
    
    @PostMapping("/{id}/add-task")
    public ResponseEntity<TaskResponseDTO> createTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskCreateDTO createDTO) {
        TaskResponseDTO response = taskService.createTask(id, createDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }




}
