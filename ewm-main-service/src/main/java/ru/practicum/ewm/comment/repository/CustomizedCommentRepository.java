package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomizedCommentRepository {
    List<Comment> findAllByAdminParams(Long[] users, Long[] events, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                       Pageable page);
}
