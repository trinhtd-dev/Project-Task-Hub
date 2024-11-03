package com.team11.taskmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.team11.taskmanagement.model.Task;
import com.team11.taskmanagement.dto.task.TaskCreateDTO;
import com.team11.taskmanagement.dto.task.TaskResponseDTO;
import com.team11.taskmanagement.dto.user.UserSummaryDTO;
import com.team11.taskmanagement.model.User;
import com.team11.taskmanagement.dto.task.TaskUpdateDTO;
import java.util.List;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    
    // Basic mapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "TODO")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignees", ignore = true)
    @Mapping(target = "dueDate", source = "dueDate")
    Task toEntity(TaskCreateDTO dto);
    
    // Complex mapping with custom method
    @Mapping(target = "assignees", source = "assignees")
    TaskResponseDTO toResponseDTO(Task task);
    

    // List mapping
    List<TaskResponseDTO> toResponseDTOs(List<Task> tasks);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "assignees", ignore = true)
    void updateEntity(TaskUpdateDTO updateDTO, @MappingTarget Task task);

    // Custom mapping methods
    default UserSummaryDTO toUserSummaryDTO(User user) {
        if (user == null) return null;
        
        UserSummaryDTO dto = new UserSummaryDTO();
        dto.setId(user.getId());
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}