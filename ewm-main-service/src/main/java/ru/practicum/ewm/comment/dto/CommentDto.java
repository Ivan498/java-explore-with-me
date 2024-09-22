package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Util.TIME_FORMAT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CommentDto {

    private Long id;

    private UserShortDto author;

    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime createdOn;

    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime edited;

    private EventShortDto event;

    private String text;
}
