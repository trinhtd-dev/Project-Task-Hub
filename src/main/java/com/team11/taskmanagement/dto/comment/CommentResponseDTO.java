package com.team11.taskmanagement.dto.comment;

import java.time.LocalDateTime;
import java.util.List;

import com.team11.taskmanagement.dto.user.UserSummaryDTO;

import lombok.Data;

@Data
public class CommentResponseDTO {
    private Long id;
    private String content;
    private UserSummaryDTO createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 