package com.team11.taskmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team11.taskmanagement.exception.UnauthorizedException;
import com.team11.taskmanagement.model.User;
import com.team11.taskmanagement.repository.UserRepository;
import com.team11.taskmanagement.dto.user.UserResponseDTO;
import com.team11.taskmanagement.mapper.UserMapper;
import com.team11.taskmanagement.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    // Get user by id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get users by ids
    public List<User> getUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    // Get users by project id
    public List<User> getUsersByProjectId(Long projectId) {
        return userRepository.findByProjects_Id(projectId);
    }

    // Get user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get current user
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("No authenticated user found");
        }
        
        return userRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user dto
    public UserResponseDTO getUserDTO(Long id) {
        return userMapper.toResponseDTO(getUserById(id).orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    // Get all users dto
    public List<UserResponseDTO> getAllUsersDTO() {
        return userMapper.toResponseDTOs(getAllUsers());
    }
}
