package com.team11.taskmanagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.team11.taskmanagement.exception.FileStorageException;
import com.team11.taskmanagement.exception.ResourceNotFoundException;
import com.team11.taskmanagement.model.Attachment;
import com.team11.taskmanagement.model.Project;
import com.team11.taskmanagement.repository.AttachmentRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Data
@Service
@Slf4j
public class AttachmentService {
        
    @Autowired
    private AttachmentRepository attachmentRepository;
    
    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    public Attachment saveAttachment(MultipartFile file, Project project) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ? 
                originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            
            // Upload file lên Cloudinary
            String fileUrl = cloudinaryService.uploadFile(file);
            
            // Tạo và lưu thông tin attachment
            Attachment attachment = new Attachment();
            attachment.setOriginalFileName(originalFilename);
            attachment.setStoredFileName(extractFileNameFromUrl(fileUrl));
            attachment.setContentType(file.getContentType());
            attachment.setFileSize(file.getSize());
            attachment.setProject(project);
            attachment.setUploadedBy(userService.getCurrentUser());
            attachment.setUploadedAt(LocalDateTime.now());
            attachment.setFilePath(fileUrl);
            
            return attachmentRepository.save(attachment);
            
        } catch (Exception e) {
            log.error("Failed to save attachment: {}", e.getMessage());
            throw new FileStorageException("Could not save attachment", e);
        }
    }

    private String extractFileNameFromUrl(String fileUrl) {
        String[] parts = fileUrl.split("/");
        String fileName = parts[parts.length - 1];
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName;
    }

    public void deleteAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));
            
        // Xóa file từ Cloudinary
        cloudinaryService.deleteFile(attachment.getFilePath());
        
        // Xóa record từ database
        attachmentRepository.delete(attachment);
    }

    public Attachment getAttachment(Long id) {
        return attachmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));
    }

    public List<Attachment> getAttachmentsByProjectId(Long projectId) {
        return attachmentRepository.findByProjectId(projectId);
    }

    public List<Attachment> getAttachmentsByTaskId(Long taskId) {
        return attachmentRepository.findByTaskId(taskId);
    }
} 