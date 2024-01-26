package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailException extends RuntimeException{
    public EmailException(String message) {
        super(message);
        log.warn(message);
    }
}
