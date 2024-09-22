package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentShortDto;
import ru.practicum.ewm.comment.service.CommentService;
import ru.practicum.ewm.util.Util;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentShortDto> getCommentsByAdmin(@RequestParam(required = false) Long[] users,
                                                    @RequestParam(required = false) Long[] events,
                                                    @RequestParam(required = false)
                                                    @DateTimeFormat(pattern = Util.TIME_FORMAT) LocalDateTime rangeStart,
                                                    @RequestParam(required = false)
                                                    @DateTimeFormat(pattern = Util.TIME_FORMAT) LocalDateTime rangeEnd,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос на просмотр комментариев админом. Параметры запроса: " +
                "users={}, events={}, rangeStart={}, rangeEnd={}, from={}, size={}", users, events, rangeStart,
                rangeEnd, from, size);

        return commentService.getAllCommentsByAdmin(users, events, rangeStart, rangeEnd, from, size);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        log.info("Получен запрос на удаление комментария с id={} от админа.");
        commentService.deleteCommentByAdmin(commentId);
    }

}
