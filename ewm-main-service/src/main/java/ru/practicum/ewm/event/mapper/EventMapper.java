package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.util.Util;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {
    public Event toEvent(NewEventDto newEventDto, Category category, User user) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .eventDate(newEventDto.getEventDate())
                .description(newEventDto.getDescription())
                .initiator(user)
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }

    public EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long views) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .description(event.getDescription())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .description(event.getDescription())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(0L)
                .build();
    }

    public Event updateEvent(Event event, UpdateEventUserRequest update) {
        return event.toBuilder()
                .annotation(update.getAnnotation() != null ? update.getAnnotation() : event.getAnnotation())
                .description(update.getDescription() != null ? update.getDescription() : event.getDescription())
                .eventDate(update.getEventDate() != null ? update.getEventDate() : event.getEventDate())
                .location(update.getLocation() != null ? update.getLocation() : event.getLocation())
                .paid(update.getPaid() != null ? update.getPaid() : event.getPaid())
                .participantLimit(update.getParticipantLimit() != null ? update.getParticipantLimit() : event.getParticipantLimit())
                .requestModeration(update.getRequestModeration() != null ? update.getRequestModeration() : event.getRequestModeration())
                .title(update.getTitle() != null ? update.getTitle() : event.getTitle())
                .build();
    }

    public Event updateEvent(Event event, UpdateEventAdminRequest update) {
        return event.toBuilder()
                .annotation(update.getAnnotation() != null ? update.getAnnotation() : event.getAnnotation())
                .description(update.getDescription() != null ? update.getDescription() : event.getDescription())
                .eventDate(update.getEventDate() != null ? update.getEventDate() : event.getEventDate())
                .location(update.getLocation() != null ? update.getLocation() : event.getLocation())
                .paid(update.getPaid() != null ? update.getPaid() : event.getPaid())
                .participantLimit(update.getParticipantLimit() != null ? update.getParticipantLimit() : event.getParticipantLimit())
                .requestModeration(update.getRequestModeration() != null ? update.getRequestModeration() : event.getRequestModeration())
                .title(update.getTitle() != null ? update.getTitle() : event.getTitle())
                .build();
    }

    public EventShortDto toEventShortDto(Event event, Long confirmedRequests, Long views) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(Util.toStringTime(event.getEventDate()))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }

}
