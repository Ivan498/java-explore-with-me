package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.model.IRequestCount;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.util.Util;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;

    @Value("${app.name}")
    private String appName;

    @Override
    public Event addEvent(Long userId, NewEventDto newEventDto) {
        log.debug("Обработка запроса на добавление события {}", newEventDto.toString());
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(Category.class, newEventDto.getCategory()));
        Event eventToSave = EventMapper.toEvent(newEventDto, category, user);
        return eventRepository.save(eventToSave);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getUserEventFullDtoById(Long userId, Long eventId) {
        log.debug("Обработка запроса просмотра события id={} пользователя id={}", eventId, userId);
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
        Map<Long, Long> confirmedRequestCounts = fetchConfirmedRequests(Set.of(eventId));
        Map<Long, Long> views = fetchEndpointStats(Set.of(eventId));
        return EventMapper.toEventFullDto(event, confirmedRequestCounts.get(eventId), views.get(eventId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        log.debug("Обработка запроса на просмотр событий пользователя id={} c {} по {}", userId, from, size);
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        List<Event> events = eventRepository.findAllByInitiatorId(userId, Util.page(from, size));
        Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> confirmedRequestCounts = fetchConfirmedRequests(eventIds);
        Map<Long, Long> views = fetchEndpointStats(eventIds);

        return events.stream()
                .map(e -> EventMapper.toEventShortDto(e,
                        confirmedRequestCounts.get(e.getId()),
                        views.get(e.getId()))
                )
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllEventFullDtoByAdmin(Long[] users,
                                                  EventState[] states,
                                                  Long[] categories,
                                                  LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd,
                                                  Integer from,
                                                  Integer size) {
        log.debug("Обработка запроса на поиск событий админом");
        List<Event> events = eventRepository.findAllByAdminParams(users, states, categories, rangeStart, rangeEnd,
                Util.page(from, size, Sort.by(Sort.Direction.DESC, "eventDate")));

        Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> confirmedRequestCounts = fetchConfirmedRequests(eventIds);
        Map<Long, Long> views = fetchEndpointStats(eventIds);

        return events.stream()
                .map(e -> EventMapper.toEventFullDto(e,
                        confirmedRequestCounts.get(e.getId()),
                        views.get(e.getId()))
                )
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllEventShortDtoByPublic(String text, Long[] categories, Boolean paid,
                                                           LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                           Boolean onlyAvailable, EventSort sort,
                                                           Integer from, Integer size, HttpServletRequest request) {
        log.debug("Обработка публичного запроса на просмотр событий");

        if (rangeEnd != null && rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            throw new ValidationException("Диапазон времени указан неверно.");
        }
        List<Event> events = eventRepository.findAllByPublicParams(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, Util.page(from, size, Sort.by(Sort.Direction.DESC, "eventDate")));

        Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> confirmedRequestCounts = fetchConfirmedRequests(eventIds);
        Map<Long, Long> views = fetchEndpointStats(eventIds);

        Comparator<EventShortDto> eventComparator;
        if (sort == null) {
            eventComparator = Comparator.comparing(EventShortDto::getEventDate);
        } else {
            switch (sort) {
                case VIEWS:
                    eventComparator = Comparator.comparing(EventShortDto::getViews);
                    break;
                case EVENT_DATE:
                    eventComparator = Comparator.comparing(EventShortDto::getEventDate);
                    break;
                default:
                    eventComparator = Comparator.comparing(EventShortDto::getEventDate);
            }
        }

        EndpointHit endpointHit = EndpointHit.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(Util.toStringTime(Util.now()))
                .build();
        statsClient.createHit(endpointHit);

        return events.stream()
                .map(e -> EventMapper.toEventShortDto(e,
                        confirmedRequestCounts.get(e.getId()),
                        views.get(e.getId()))
                )
                .sorted(eventComparator)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByPublic(Long id, HttpServletRequest request) {
        log.debug("Обработка публичного запроса на просмотр события с id={}");
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(Event.class, id));
        Map<Long, Long> confirmedRequestCounts = fetchConfirmedRequests(Set.of(id));
        Map<Long, Long> views = fetchEndpointStats(Set.of(id));

        EndpointHit endpointHit = EndpointHit.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(Util.toStringTime(Util.now()))
                .build();
        statsClient.createHit(endpointHit);

        return EventMapper.toEventFullDto(event, confirmedRequestCounts.get(id), views.get(id));
    }

    @Override
    public Event updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.debug("Обработка запроса на обновление события id={} пользователя id={}. Содержание: {}",
                eventId,
                userId,
                updateEventUserRequest.toString());

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Пользователь не инициатор события");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Опубликованные события редактировать нельзя.");
        }
        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    throw new ValidationException(String.format("Статуса %s не существует",
                            updateEventUserRequest.getStateAction().toString()));
            }
        }
        if (updateEventUserRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException(Category.class, updateEventUserRequest.getCategory()));
            event.setCategory(category);
        }
        Event eventForUpdate = EventMapper.updateEvent(event, updateEventUserRequest);
        return eventRepository.save(eventForUpdate);
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("Обработка запроса на обновления события с id={} админом", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(Event.class, eventId));
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Опубликованные события редактировать нельзя.");
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            switch (updateEventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    if (event.getState() != EventState.PENDING) {
                        throw new ConflictException("Публиковать можно только события в состоянии ожидания.");
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(Util.now());
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    throw new ValidationException(String.format("Статуса %s не существует",
                            updateEventAdminRequest.getStateAction().toString()));
            }
        }
        Event eventForUpdate = EventMapper.updateEvent(event, updateEventAdminRequest);
        Map<Long, Long> confirmedRequestCounts = fetchConfirmedRequests(Set.of(eventId));
        Map<Long, Long> views = fetchEndpointStats(Set.of(eventId));
        Event savedEvent = eventRepository.save(eventForUpdate);
        return EventMapper.toEventFullDto(savedEvent, confirmedRequestCounts.get(eventId), views.get(eventId));
    }

    @Override
    public List<EventShortDto> makeEventShortDtoList(List<Event> events) {
        if (events == null || events.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> confirmedRequests = fetchConfirmedRequests(eventIds);
        Map<Long, Long> views = fetchEndpointStats(eventIds);

        return events.stream().map(e -> EventMapper.toEventShortDto(e,
                confirmedRequests.get(e.getId()),
                views.get(e.getId())))
                .collect(Collectors.toList());
    }

    private Map<Long, Long> fetchConfirmedRequests(Set<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<IRequestCount> requestCounts = requestRepository.countAllByStatusAndEventIdIn(RequestStatus.CONFIRMED,
                eventIds);
        Map<Long, Long> eventRequestCounts = requestCounts.stream()
                .collect(Collectors.toMap(IRequestCount::getEventRequests, IRequestCount::getCountRequests));
        eventIds.forEach(id -> eventRequestCounts.putIfAbsent(id, 0L));

        return eventRequestCounts;
    }

    private Map<Long, Long> fetchEndpointStats(Set<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, Long> eventStats = new HashMap<>();

        String[] uris = eventIds.stream().map(id -> "/events/" + id).toArray(String[]::new);
        List<ViewStats> viewStats = statsClient.getStats(
                Util.now().minusYears(1),
                Util.now(),
                uris,
                true)
                .getBody();

        viewStats.forEach(view -> {
            String[] parts = view.getUri().split("/");
            Long eventId = Long.valueOf(parts[parts.length - 1]);
            eventStats.put(eventId, view.getHits());
        });
        eventIds.forEach(id -> eventStats.putIfAbsent(id, 0L));

        return eventStats;
    }

}
