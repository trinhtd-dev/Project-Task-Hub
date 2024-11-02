package com.team11.taskmanagement.dto.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import com.team11.taskmanagement.dto.user.UserSummaryDTO;
import com.team11.taskmanagement.model.TaskStatus;
@Data
public class TaskResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate dueDate;
    private TaskStatus status;
    private List<UserSummaryDTO> assignees;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}