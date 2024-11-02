package com.team11.taskmanagement.dto.user;

import lombok.Data;

@Data
public class UserSummaryDTO {
    private Long id;
    private String name;
    private String email;
    private String avatarUrl;
}