package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.validation.DateMustBeAfter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Util.TIME_FORMAT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    private String annotation;

    @Positive
    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @DateMustBeAfter(hours = 1)
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private AdminStateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
