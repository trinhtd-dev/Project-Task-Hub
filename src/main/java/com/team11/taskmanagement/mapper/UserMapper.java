package com.team11.taskmanagement.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.team11.taskmanagement.dto.user.UserResponseDTO;
import com.team11.taskmanagement.dto.user.UserSummaryDTO;
import com.team11.taskmanagement.model.User;
import com.team11.taskmanagement.dto.user.UserCreateDTO;
import com.team11.taskmanagement.dto.user.UserUpdateDTO;
@Mapper(componentModel = "spring")
public interface UserMapper {
// Create user
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(UserCreateDTO dto);

// Summary user
    @Mapping(target = "name", source = "fullName")
    UserSummaryDTO toSummaryDTO(User user);

    Set<UserSummaryDTO> toSummaryDTOs(Set<User> users);

// Response user
    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toResponseDTOs(List<User> users);

// Update user
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntity(UserUpdateDTO dto, @MappingTarget User user);
} 