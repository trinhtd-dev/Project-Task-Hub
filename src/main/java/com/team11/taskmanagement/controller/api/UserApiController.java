package com.team11.taskmanagement.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team11.taskmanagement.dto.user.UserResponseDTO;
import com.team11.taskmanagement.dto.user.UserUpdateDTO;
import com.team11.taskmanagement.dto.password.OtpVerificationDTO;
import com.team11.taskmanagement.dto.password.ChangePasswordDTO;

import com.team11.taskmanagement.service.UserService;
import com.team11.taskmanagement.exception.InvalidOtpException;
import com.team11.taskmanagement.exception.InvalidPasswordException;
import com.team11.taskmanagement.exception.ResourceNotFoundException;


@RestController
@RequestMapping("/api/users")
public class UserApiController {
    @Autowired
    private UserService userService;

    // Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserDTO(id));
    }

    // Update user
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDTO));
    }

    // Reset password
    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable Long id) {
        return ResponseEntity.ok(userService.resetPassword(id));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    // Send OTP
    @PostMapping("/profile/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        userService.sendChangePasswordOtp(email);
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/profile/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationDTO request) {
        userService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok("OTP verified successfully");
    }

    @PostMapping("/profile/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO request) {
        System.out.println("Request: ----------------------------------");
        System.out.println(request);
        return ResponseEntity.ok("Password changed successfully");
    }
}
