package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllEventsPublic(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) Long[] categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(pattern = Util.TIME_FORMAT) LocalDateTime rangeStart,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(pattern = Util.TIME_FORMAT) LocalDateTime rangeEnd,
                                                  @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(required = false) EventSort sort,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size,
                                                  HttpServletRequest request) {
        log.info("Получен публичный запрос на просмотр событий");

        return eventService.getAllEventShortDtoByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventPublic(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен публичный запрос на просмотр события с id={}", id);
        return eventService.getEventByPublic(id, request);
    }
}
