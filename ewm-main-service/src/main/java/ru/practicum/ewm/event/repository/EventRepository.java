package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Long>, CustomizedEventRepository {

    Optional<Event> findByInitiatorIdAndId(Long userId, Long eventId);

    List<Event> findAllByInitiatorId(Long userId, Pageable page);

    Optional<Event> findByIdAndState(Long id, EventState state);

    List<Event> findAllByIdIn(List<Long> ids);
}
