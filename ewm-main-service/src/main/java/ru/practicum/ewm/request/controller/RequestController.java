package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto postRequest(@PathVariable Long userId,
                                               @RequestParam Long eventId) {
        log.info("Получен запрос на участие в событии id={} от пользователя id={}", eventId, userId);
        return RequestMapper.toDto(requestService.addRequest(userId, eventId));
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        log.info("Просмотр запросов на участие пользователя с id={}", userId);
        return RequestMapper.toDtoList(requestService.getAllUserRequests(userId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable Long userId,
                                                       @PathVariable Long requestId) {
        log.info("Получен запрос на отмену участия пользователя id={} в событии id={}", userId, requestId);
        return RequestMapper.toDto(requestService.cancelRequestByUser(userId, requestId));
    }
}
