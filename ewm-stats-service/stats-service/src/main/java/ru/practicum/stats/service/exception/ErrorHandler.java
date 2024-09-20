package ru.practicum.stats.service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIllegalArgumentException(final IllegalArgumentException e) {
        log.warn(e.getMessage());
        return ApiError.builder()
                .errors(e.getStackTrace())
                .message(e.getMessage())
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return ApiError.builder()
                .errors(e.getStackTrace())
                .message(e.getMessage())
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final MissingServletRequestParameterException e) {
        log.warn(e.getMessage());
        return ApiError.builder()
                .errors(e.getStackTrace())
                .message(e.getMessage())
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handle(final Throwable e) {
        log.error(e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Internal server error")
                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                .timestamp(LocalDateTime.now())
                .build();
    }
}
