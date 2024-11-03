package com.team11.taskmanagement.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.team11.taskmanagement.dto.task.TaskResponseDTO;
import com.team11.taskmanagement.dto.task.TaskUpdateDTO;
import com.team11.taskmanagement.service.TaskService;


@RestController
public class TaskApiController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/api/tasks/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PatchMapping("/api/tasks/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdateDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskUpdateDTO));
    }
}
