package com.team11.taskmanagement.service;

import com.cloudinary.Cloudinary;
import com.team11.taskmanagement.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    
    private final Cloudinary cloudinary;
    
    public String uploadImage(MultipartFile file) {
        try {
            // Create upload parameters
            Map<String, Object> params = new HashMap<>();
            params.put("folder", "avatars");
            params.put("width", 300);
            params.put("height", 300);
            params.put("crop", "fill");
            
            // Upload file
            Map result = cloudinary.uploader().upload(file.getBytes(), params);
            
            return result.get("secure_url").toString();
            
        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary: {}", e.getMessage());
            throw new FileUploadException("Could not upload image", e);
        }
    }
    
    public void deleteImage(String imageUrl) {
        try {
            // Extract public ID including folder
            String[] urlParts = imageUrl.split("/");
            int uploadIndex = Arrays.asList(urlParts).indexOf("upload");
            if (uploadIndex >= 0 && urlParts.length > uploadIndex + 2) {
                String publicId = String.join("/", Arrays.copyOfRange(urlParts, uploadIndex + 2, urlParts.length));
                // Remove file extension
                publicId = publicId.substring(0, publicId.lastIndexOf('.'));
                cloudinary.uploader().destroy(publicId, Map.of());
            }
        } catch (IOException e) {
            log.error("Failed to delete image from Cloudinary: {}", imageUrl, e);
        }
    }
} 