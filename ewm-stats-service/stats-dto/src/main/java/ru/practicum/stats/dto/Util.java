package ru.practicum.stats.dto;

import lombok.experimental.UtilityClass;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    public static String encode(LocalDateTime dateTime) {
        String stringDate = dateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
        return URLEncoder.encode(stringDate, StandardCharsets.UTF_8);
    }

    public LocalDateTime decode(String dateString) {
        String decodedString = URLDecoder.decode(dateString, StandardCharsets.UTF_8);
        return LocalDateTime.parse(decodedString, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }
}
