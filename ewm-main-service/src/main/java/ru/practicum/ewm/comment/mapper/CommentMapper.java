package ru.practicum.ewm.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentShortDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public CommentDto toDto(Comment savedComment, UserShortDto userShortDto, EventShortDto eventShortDto) {
        return CommentDto.builder()
                .id(savedComment.getId())
                .author(userShortDto)
                .createdOn(savedComment.getCreatedOn().toLocalDateTime())
                .edited(savedComment.getEdited())
                .event(eventShortDto)
                .text(savedComment.getText())
                .build();
    }

    public CommentShortDto toShortDto(Comment comment) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .createdOn(comment.getCreatedOn().toLocalDateTime())
                .edited(comment.getEdited())
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .build();
    }

    public List<CommentShortDto> toShortDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toShortDto).collect(Collectors.toList());
    }

    public Comment updateComment(Comment comment, NewCommentDto commentDto) {
        return Comment.builder()
                .author(comment.getAuthor())
                .edited(comment.getEdited())
                .createdOn(comment.getCreatedOn())
                .event(comment.getEvent())
                .id(comment.getId())
                .text(commentDto.getText() == null ? comment.getText() : commentDto.getText())
                .build();
    }
}
