package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.model.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    Event addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getUserEventFullDtoById(Long userId, Long eventId);

    Event updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    List<EventFullDto> getAllEventFullDtoByAdmin(Long[] users,
                                           EventState[] states,
                                           Long[] categories,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Integer from,
                                           Integer size);

    List<EventShortDto> getAllEventShortDtoByPublic(String text,
                                                    Long[] categories,
                                                    Boolean paid,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable,
                                                    EventSort sort,
                                                    Integer from,
                                                    Integer size,
                                                    HttpServletRequest request);

    EventFullDto getEventByPublic(Long id, HttpServletRequest request);

    List<EventShortDto> makeEventShortDtoList(List<Event> events);
}
