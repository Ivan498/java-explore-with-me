package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentShortDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    CommentDto addComment(Long userId, Long eventId, NewCommentDto commentDto);

    List<CommentShortDto> getAllCommentsByUser(Long eventId, Integer from, Integer size);

    CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto commentDto);

    void deleteComment(Long userId, Long eventId, Long commentId);

    List<CommentShortDto> getAllCommentsByAdmin(Long[] users, Long[] events, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                           Integer from, Integer size);

    void deleteCommentByAdmin(Long commentId);
}
