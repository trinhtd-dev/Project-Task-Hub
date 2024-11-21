package com.team11.taskmanagement.dto.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.team11.taskmanagement.dto.user.UserSummaryDTO;
import com.team11.taskmanagement.model.TaskStatus;

import lombok.Data;

@Data
public class TaskResponseDTO {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;
    private String tag;
    private List<UserSummaryDTO> assignees;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long projectId;
    private String projectName;
}