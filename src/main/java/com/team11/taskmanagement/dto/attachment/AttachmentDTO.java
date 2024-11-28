package com.team11.taskmanagement.dto.attachment;

import java.time.LocalDateTime;

import com.team11.taskmanagement.dto.user.UserSummaryDTO;

import lombok.Data;

@Data
public class AttachmentDTO {
    private Long id;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private LocalDateTime uploadedAt;
    private UserSummaryDTO uploadedBy;
} 