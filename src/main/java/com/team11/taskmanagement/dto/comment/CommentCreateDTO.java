package com.team11.taskmanagement.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreateDTO {
    @NotBlank(message = "Nội dung không được để trống")
    private String content;
    private Long parentId;  // Optional, for reply comments
} 