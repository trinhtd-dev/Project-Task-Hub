package com.team11.taskmanagement.service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team11.taskmanagement.dto.task.TaskCreateDTO;
import com.team11.taskmanagement.dto.task.TaskResponseDTO;
import com.team11.taskmanagement.mapper.TaskMapper;
import com.team11.taskmanagement.model.Task;
import com.team11.taskmanagement.repository.TaskRepository;
import com.team11.taskmanagement.model.User;
import com.team11.taskmanagement.model.Project;
import com.team11.taskmanagement.repository.ProjectRepository;
import com.team11.taskmanagement.exception.ResourceNotFoundException;

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
    
    public List<TaskResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toResponseDTOs(tasks);
    }
}
