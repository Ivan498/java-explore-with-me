package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentShortDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.service.CommentService;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.PermissionDeniedException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable Long userId,
                                  @PathVariable Long eventId,
                                  @Valid @RequestBody NewCommentDto commentDto) {
        log.debug("Получен запрос на добавление комментария к событию id={} от пользователя id={}: {}",
                eventId, userId, commentDto.toString());
        return commentService.addComment(userId, eventId, commentDto);
    }

    @GetMapping
    public List<CommentShortDto> getCommentsByUser(@PathVariable Long eventId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("Получен запрос на просмотр комментариев к событию id={} from={} size={}", eventId, from, size);
        return commentService.getAllCommentsByUser(eventId, from, size);
    }

    @PatchMapping("/{commentId}")
    public CommentDto patchComment(@PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @PathVariable Long commentId,
                                   @Valid @RequestBody NewCommentDto commentDto) {
        log.debug("Получен запрос на редактирование комментария id={}, eventId={}, userId={}: {}",
                commentId, eventId, userId, commentDto.toString());
        return commentService.updateComment(userId, eventId, commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long eventId,
                              @PathVariable Long commentId) {
        log.debug("Получен запрос на удаление комментария id={} от пользователя id={}", commentId, userId);
        commentService.deleteComment(userId, eventId, commentId);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(NotFoundException e) {
        log.error("Пользователь не найден: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(PermissionDeniedException e) {
        log.error("Доступ запрещен: {}", e.getMessage());
        return e.getMessage();
    }
}
