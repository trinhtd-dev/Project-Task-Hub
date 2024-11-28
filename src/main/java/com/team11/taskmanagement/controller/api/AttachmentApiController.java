package com.team11.taskmanagement.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team11.taskmanagement.dto.attachment.AttachmentResponseDTO;
import com.team11.taskmanagement.exception.ResourceNotFoundException;
import com.team11.taskmanagement.mapper.AttachmentMapper;
import com.team11.taskmanagement.model.Attachment;
import com.team11.taskmanagement.model.Project;
import com.team11.taskmanagement.service.AttachmentService;
import com.team11.taskmanagement.service.ProjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/attachments")
@Slf4j
@RequiredArgsConstructor
public class AttachmentApiController {

    private final AttachmentService attachmentService;
    private final ProjectService projectService;
    private final AttachmentMapper attachmentMapper;

    // Get attachments by project id
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<AttachmentResponseDTO>> getAttachmentsByProjectId(@PathVariable Long projectId) {
        List<Attachment> attachments = attachmentService.getAttachmentsByProjectId(projectId);
        return ResponseEntity.ok(attachments.stream().map(attachmentMapper::toResponseDTO).collect(Collectors.toList()));
    }

    // Get attachments by task id
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<AttachmentResponseDTO>> getAttachmentsByTaskId(@PathVariable Long taskId) {
        List<Attachment> attachments = attachmentService.getAttachmentsByTaskId(taskId);
        return ResponseEntity.ok(attachments.stream().map(attachmentMapper::toResponseDTO).collect(Collectors.toList()));
    }

    @PostMapping("/upload/{projectId}")
    public ResponseEntity<AttachmentResponseDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long projectId) {
        try {
            log.info("Uploading file: {} to project: {}", file.getOriginalFilename(), projectId);
            
            Project project = projectService.getProjectById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
                
            Attachment savedAttachment = attachmentService.saveAttachment(file, project);
            return ResponseEntity.ok(attachmentMapper.toResponseDTO(savedAttachment));
            
        } catch (Exception e) {
            log.error("Error uploading file: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        try {
            attachmentService.deleteAttachment(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting file: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttachmentResponseDTO> getFile(@PathVariable Long id) {
        try {
            Attachment attachment = attachmentService.getAttachment(id);
            return ResponseEntity.ok(attachmentMapper.toResponseDTO(attachment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
