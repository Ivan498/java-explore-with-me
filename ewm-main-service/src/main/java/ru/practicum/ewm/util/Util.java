package ru.practicum.ewm.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class Util {
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public LocalDateTime toLocalDateTime(String dateString) {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    public String toStringTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    public Pageable page(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    public Pageable page(int from, int size, Sort sort) {
        return PageRequest.of(from > 0 ? from / size : 0, size, sort);
    }
}

