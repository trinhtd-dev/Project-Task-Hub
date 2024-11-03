package com.team11.taskmanagement.dto.task;

import java.time.LocalDate;
import java.util.List;

import com.team11.taskmanagement.model.TaskStatus;

import lombok.Data;

@Data
public class TaskUpdateDTO {
    private String name;
    private String description;
    private LocalDate dueDate;
    private TaskStatus status;
    private List<Long> assigneeIds;
}
