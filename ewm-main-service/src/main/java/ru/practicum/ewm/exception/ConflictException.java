package ru.practicum.ewm.exception;

public class ConflictException extends RuntimeException {

    public ConflictException() {

    }

    public ConflictException(final String message) {
        super(message);
    }
}
