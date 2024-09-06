package ru.practicum.stats.dto;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class Util {
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime toLocalDateTime(String dateString) {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    public static String toStringTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }
}
