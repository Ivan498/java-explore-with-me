package ru.practicum.ewm.exception;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {

    }

    public PermissionDeniedException(final String message) {
        super(message);
    }

    public PermissionDeniedException(final String message, Throwable cause) {
        super(message, cause);
    }
}
