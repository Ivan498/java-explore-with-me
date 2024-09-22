package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.util.Util;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CommentShortDto {

    private Long id;

    private String authorName;

    @JsonFormat(pattern = Util.TIME_FORMAT)
    private LocalDateTime createdOn;

    private LocalDateTime edited;

    private Long eventId;

    private String text;


}
