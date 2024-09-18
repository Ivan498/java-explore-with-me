package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.util.Util;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) Long[] users,
                                               @RequestParam(required = false) EventState[] states,
                                               @RequestParam(required = false) Long[] categories,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = Util.TIME_FORMAT) LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = Util.TIME_FORMAT) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос на поиск событий админом");
        return eventService.getAllEventFullDtoByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEventByAdmin(@PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Получен запрос на изменение события id={} админом", eventId);
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequest);
    }
}
