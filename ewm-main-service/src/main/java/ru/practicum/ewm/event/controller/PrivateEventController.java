package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateEventController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Получен запрос на добавление события {} от пользователя с id={}", newEventDto.toString(), userId);
        Event event = eventService.addEvent(userId, newEventDto);
        return EventMapper.toEventFullDto(event);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получен запрос просмотра события id={} пользователя id={}", eventId, userId);
        return eventService.getUserEventFullDtoById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEventByUser(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Получен запрос на обновление события id={} пользователя id={}. Содержание: {}",
                eventId,
                userId,
                updateEventUserRequest.toString()
        );

        return EventMapper.toEventFullDto(eventService.updateEventByUser(userId, eventId, updateEventUserRequest));
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос на просмотр событий пользователя id={} c {} по {}", userId, from, size);
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestsByInitiator(@PathVariable Long userId,
                                                                   @PathVariable Long eventId) {
        log.info("Получен запрос на просмотр заявок на участие в событии id={} пользователя id={}", eventId, userId);
        return RequestMapper.toDtoList(requestService.getAllEventRequestsByInitiator(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequestsStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @Valid
                                                              @RequestBody EventRequestStatusUpdateRequest update) {
        log.info("Получен запрос на изменение статусов заявок события id={} пользователя id={}", eventId, userId);
        return requestService.updateRequestsByInitiator(userId, eventId, update);
    }

}
