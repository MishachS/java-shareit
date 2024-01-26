package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OwnerException extends RuntimeException {
    public OwnerException(String message) {
        super(message);
        log.warn(message);
    }
}
