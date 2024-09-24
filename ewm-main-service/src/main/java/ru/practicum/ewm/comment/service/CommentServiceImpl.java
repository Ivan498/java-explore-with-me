package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentShortDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.util.Util;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto commentDto) {
        log.debug("Обработка запроса на добавления комментария к событию id={} от пользователя id={}: {}",
                eventId, userId, commentDto.toString());

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(Event.class, eventId));

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Комментировать можно только опубликованные события");
        }

        UserShortDto userShortDto = UserMapper.toUserShortDto(user);
        EventShortDto eventShortDto = eventService.makeEventShortDtoList(List.of(event)).get(0);
        log.trace("Полученные Dto пользователя={} и события={}", userShortDto.toString(), eventShortDto.toString());

        Comment comment = Comment.builder().author(user).event(event).text(commentDto.getText()).build();
        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toDto(savedComment, userShortDto, eventShortDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentShortDto> getAllCommentsByUser(Long eventId, Integer from, Integer size) {
        log.debug("Обработка запроса на просмотр комментариев к событию id={} c {} по {}", eventId, from, size);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(Event.class, eventId));
        Pageable page = Util.page(from, size, Sort.by(Sort.Direction.ASC, "createdOn"));
        List<Comment> comments = commentRepository.findAllByEventId(eventId, page);

        return CommentMapper.toShortDtoList(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentShortDto> getAllCommentsByAdmin(Long[] users, Long[] events, LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd, Integer from, Integer size) {
        log.debug("Обработка запроса на просмотр комментариев админом. Параметры: users={}, events={}, rangeStart={}," +
                " rangeEnd={}, from={}, size={}", users, events, rangeStart, rangeEnd, from, size);

        Pageable page = Util.page(from, size, Sort.by(Sort.Direction.DESC, "createdOn"));
        List<Comment> comments = commentRepository.findAllByAdminParams(users, events, rangeStart, rangeEnd, page);

        return CommentMapper.toShortDtoList(comments);
    }

    @Override
    public CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto commentDto) {
        log.debug("Обработка запроса на редактирование комментария id={}, eventId={}, userId={}: {}",
                commentId, eventId, userId, commentDto.toString());
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(Event.class, eventId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class, commentId));

        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new ConflictException("Только автор комментария может редактировать его.");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Писать комментарии и редактировать их можно только для опубликованных событий.");
        }

        UserShortDto userShortDto = UserMapper.toUserShortDto(user);
        EventShortDto eventShortDto = eventService.makeEventShortDtoList(List.of(event)).get(0);
        log.trace("Полученные Dto пользователя={} и события={}", userShortDto.toString(), eventShortDto.toString());

        Comment commentToUpdate = CommentMapper.updateComment(comment, commentDto);
        commentToUpdate.setEdited(Util.now());
        Comment savedComment = commentRepository.save(commentToUpdate);
        log.debug("Комментарий id={} сохранен в базу: {}", savedComment.getId(), savedComment);

        return CommentMapper.toDto(savedComment, userShortDto, eventShortDto);
    }

    @Override
    public void deleteComment(Long userId, Long eventId, Long commentId) {
        log.debug("Получен запрос на удаление комментария id={} от пользователя id={}", commentId, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(Event.class, eventId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class, commentId));

        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new ConflictException("Только автор  комментария  может удалить его.");
        }

        commentRepository.deleteById(commentId);
        log.debug("Комментарий id={} удалён пользователем {}", commentId, user.getId());
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        log.debug("Обработка запроса на удаление комментария с id={} от админа.");
        commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class, commentId));

        commentRepository.deleteById(commentId);
        log.debug("Комментарий id={} удалён админом", commentId);
    }
}
