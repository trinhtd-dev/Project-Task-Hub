package com.team11.taskmanagement.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team11.taskmanagement.dto.task.TaskCreateDTO;
import com.team11.taskmanagement.dto.task.TaskResponseDTO;
import com.team11.taskmanagement.dto.task.TaskUpdateDTO;
import com.team11.taskmanagement.exception.ResourceNotFoundException;
import com.team11.taskmanagement.mapper.TaskMapper;
import com.team11.taskmanagement.model.Project;
import com.team11.taskmanagement.model.Task;
import com.team11.taskmanagement.model.User;
import com.team11.taskmanagement.repository.ProjectRepository;
import com.team11.taskmanagement.repository.TaskRepository;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    
    public TaskResponseDTO createTask(Long projectId, TaskCreateDTO createDTO) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
            
        User currentUser = userService.getCurrentUser();
        
        Task task = taskMapper.toEntity(createDTO);
        task.setProject(project);
        task.setCreatedBy(currentUser);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setIsDeleted(false);

        // Set assignees if provided in DTO
        if (createDTO.getAssigneeIds() != null && !createDTO.getAssigneeIds().isEmpty()) {
            Set<User> assignees = new HashSet<>(userService.getUsersByIds(createDTO.getAssigneeIds()));
            task.setAssignees(assignees);
        }
        
        Task savedTask = taskRepository.save(task);
        
        return taskMapper.toResponseDTO(savedTask);
    }
    
    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return taskMapper.toResponseDTO(task);
    }
    
    public TaskResponseDTO updateTask(Long id, TaskUpdateDTO updateDTO) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        taskMapper.updateEntity(updateDTO, task);
        task.setUpdatedAt(LocalDateTime.now());
        
        // Update assignees if provided
        if (updateDTO.getAssigneeIds() != null) {
            Set<User> assignees = new HashSet<>(userService.getUsersByIds(updateDTO.getAssigneeIds()));
            task.setAssignees(assignees);
        }
        
        return taskMapper.toResponseDTO(taskRepository.save(task));
    }
    
    public List<TaskResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toResponseDTOs(tasks);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
    }
}
