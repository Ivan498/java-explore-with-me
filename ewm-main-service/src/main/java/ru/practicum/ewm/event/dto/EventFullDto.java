package ru.practicum.ewm.event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Util.TIME_FORMAT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EventFullDto {
    private String annotation;

    private Category category;

    private Long confirmedRequests;

    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;

    private String title;

    private Long views;
}
