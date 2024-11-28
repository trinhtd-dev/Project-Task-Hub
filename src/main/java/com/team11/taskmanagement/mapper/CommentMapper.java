package com.team11.taskmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.team11.taskmanagement.model.Comment;
import com.team11.taskmanagement.dto.comment.CommentResponseDTO;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class}
)
public interface CommentMapper {
    @Mapping(target = "createdBy", source = "createdBy")
    CommentResponseDTO toCommentResponseDTO(Comment comment);
}
