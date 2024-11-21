package com.team11.taskmanagement.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team11.taskmanagement.dto.user.UserResponseDTO;
import com.team11.taskmanagement.dto.user.UserSummaryDTO;
import com.team11.taskmanagement.model.User;
import com.team11.taskmanagement.dto.user.UserCreateDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(UserCreateDTO dto);

    @Mapping(target = "name", source = "fullName")
    UserSummaryDTO toSummaryDTO(User user);
    
    Set<UserSummaryDTO> toSummaryDTOs(Set<User> users);

    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toResponseDTOs(List<User> users);
} 