package ru.practicum.stats.service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static ru.practicum.stats.dto.Util.TIME_FORMAT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ApiError {

    private StackTraceElement[] errors;

    private String message;

    private String reason;

    private String status;

    @DateTimeFormat(pattern = TIME_FORMAT)
    private LocalDateTime timestamp;
}
