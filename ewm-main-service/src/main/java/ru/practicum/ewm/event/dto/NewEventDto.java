package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.validation.DateMustBeAfter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Util.TIME_FORMAT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @DateMustBeAfter(hours = 2)
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
