package com.team11.taskmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.team11.taskmanagement.dto.user.UserSummaryDTO;
import com.team11.taskmanagement.model.User;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "name", source = "fullName")
    UserSummaryDTO toSummaryDTO(User user);
    
    Set<UserSummaryDTO> toSummaryDTOs(Set<User> users);
} 