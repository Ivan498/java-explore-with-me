package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomizedEventRepository {

    List<Event> findAllByAdminParams(Long[] users, EventState[] states, Long[] categories, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Pageable page);

    List<Event> findAllByPublicParams(String text, Long[] categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable page);
}
