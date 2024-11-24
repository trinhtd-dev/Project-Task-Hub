package com.team11.taskmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team11.taskmanagement.dto.user.UserCreateDTO;
import com.team11.taskmanagement.dto.user.UserResponseDTO;
import com.team11.taskmanagement.dto.user.UserUpdateDTO;
import com.team11.taskmanagement.exception.ResourceNotFoundException;
import com.team11.taskmanagement.exception.UnauthorizedException;
import com.team11.taskmanagement.mapper.UserMapper;
import com.team11.taskmanagement.model.User;
import com.team11.taskmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
    @Transactional(readOnly = true)
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

    // Create user
    public void createUser (UserCreateDTO userCreateDTO) {
        User user = userMapper.toEntity(userCreateDTO);
        System.out.println(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // Update user
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.updateEntity(userUpdateDTO, user);
        userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    // Reset password
    public String resetPassword(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            String newPassword = RandomStringUtils.randomAlphanumeric(5);
            user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return newPassword;
    }

    // Delete user
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully";
    }
}
