package com.team11.taskmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.team11.taskmanagement.model.Task;
import com.team11.taskmanagement.dto.task.TaskCreateDTO;
import com.team11.taskmanagement.dto.task.TaskResponseDTO;
import com.team11.taskmanagement.dto.task.TaskUpdateDTO;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "TODO")
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tag", ignore = true)
    @Mapping(target = "assignees", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    Task toEntity(TaskCreateDTO dto);
    
    @Mapping(target = "assignees", source = "assignees")
    TaskResponseDTO toResponseDTO(Task task);
    
    List<TaskResponseDTO> toResponseDTOs(List<Task> tasks);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tag", ignore = true)
    @Mapping(target = "assignees", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    void updateEntity(TaskUpdateDTO dto, @MappingTarget Task task);
}