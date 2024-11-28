package com.team11.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team11.taskmanagement.model.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByProjectId(Long projectId);
    List<Attachment> findByTaskId(Long taskId);
}
