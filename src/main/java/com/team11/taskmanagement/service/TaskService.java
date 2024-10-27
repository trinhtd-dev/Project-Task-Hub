package com.team11.taskmanagement.service;

import com.team11.taskmanagement.model.Task;
import com.team11.taskmanagement.repository.TaskRepository;
import com.team11.taskmanagement.repository.ProjectRepository;
import com.team11.taskmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.team11.taskmanagement.model.Project;
import com.team11.taskmanagement.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    
    public Task createTask(Task task, Long projectId, Long assignedUserId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        User assignedUser = userRepository.findById(assignedUserId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (!project.getMembers().contains(assignedUser)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assigned user is not a member of the project");
        }
        
        task.setProject(project);
        task.setAssignee(assignedUser);
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
