package com.team11.taskmanagement.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team11.taskmanagement.dto.user.UserResponseDTO;
import com.team11.taskmanagement.dto.user.UserSummaryDTO;
import com.team11.taskmanagement.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
       
    @Mapping(target = "name", source = "fullName")
    UserSummaryDTO toSummaryDTO(User user);
    
    Set<UserSummaryDTO> toSummaryDTOs(Set<User> users);

    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toResponseDTOs(List<User> users);
} 