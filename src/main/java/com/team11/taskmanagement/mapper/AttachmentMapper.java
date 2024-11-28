package com.team11.taskmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.team11.taskmanagement.dto.attachment.AttachmentResponseDTO;
import com.team11.taskmanagement.model.Attachment;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AttachmentMapper {
    AttachmentMapper INSTANCE = Mappers.getMapper(AttachmentMapper.class);

    @Mapping(source = "uploadedBy", target = "uploadedBy")
    @Mapping(source = "uploadedAt", target = "uploadedAt")
    AttachmentResponseDTO toResponseDTO(Attachment attachment);
} 