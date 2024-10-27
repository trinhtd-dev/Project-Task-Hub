package com.team11.taskmanagement.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team11.taskmanagement.dto.ProjectDTO;
import com.team11.taskmanagement.model.Project;
import com.team11.taskmanagement.model.ProjectPriority;
import com.team11.taskmanagement.model.ProjectStatus;
import com.team11.taskmanagement.model.TagProject;
import com.team11.taskmanagement.model.Task;
import com.team11.taskmanagement.model.User;
import com.team11.taskmanagement.repository.ProjectRepository;
import com.team11.taskmanagement.repository.TaskRepository;
import com.team11.taskmanagement.repository.UserRepository;


@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }



    @Transactional
    public Project createProject(ProjectDTO projectDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setStatus(ProjectStatus.valueOf(projectDTO.getStatus()));
        project.setStartDate(projectDTO.getStartDate());
        project.setDueDate(projectDTO.getDueDate());
        project.setPriority(ProjectPriority.valueOf(projectDTO.getPriority()));
        project.setTag(TagProject.valueOf(projectDTO.getTag()));
        project.setCreatedBy(user);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        project.setIsDeleted(false);

        Set<User> members = new HashSet<>(userRepository.findAllById(projectDTO.getMemberIds()));
        members.add(user);
        project.setMembers(members);

        project = projectRepository.save(project);

        for (ProjectDTO.TaskDTO taskDTO : projectDTO.getTasks()) {
            Task task = new Task();
            task.setName(taskDTO.getName());
            task.setDescription(taskDTO.getDescription());
            task.setDueDate(taskDTO.getDueDate().atStartOfDay());
            task.setProject(project);
            task.setCreatedBy(user);
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            task.setIsDeleted(false);
            if (!taskDTO.getAssigneeIds().isEmpty()) {
                User assignee = userRepository.findById(taskDTO.getAssigneeIds().get(0))
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
                task.setAssignee(assignee);
            }
            taskRepository.save(task);
        }

        return project;
    }

    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public void addMemberToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        project.addMember(user);
        projectRepository.save(project);
    }

    @Transactional
    public void removeMemberFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Xử lý các task đã gán cho thành viên
        List<Task> userTasks = taskRepository.findByProjectAndAssignee(project, user);
        for (Task task : userTasks) {
            task.setAssignee(null); // Hoặc gán cho người khác tùy theo logic của bạn
        }
        taskRepository.saveAll(userTasks);
        
        project.removeMember(user);
        projectRepository.save(project);
    }

    public Set<User> getProjectMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return project.getMembers();
    }
}
