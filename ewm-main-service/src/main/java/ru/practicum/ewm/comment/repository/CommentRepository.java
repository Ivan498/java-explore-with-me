package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.ewm.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long>, CustomizedCommentRepository {
    List<Comment> findAllByEventId(Long eventId, Pageable page);

}
