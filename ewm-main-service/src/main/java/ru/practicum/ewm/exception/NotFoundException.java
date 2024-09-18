package ru.practicum.ewm.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message) {
        super(message);
    }

    public <T> NotFoundException(Class<T> tClass, Long id) {
        super(String.format("%s c id=%d не найден", tClass.getSimpleName(), id));
    }

    public NotFoundException(final String message, Throwable cause) {
        super(message, cause);
    }
}
