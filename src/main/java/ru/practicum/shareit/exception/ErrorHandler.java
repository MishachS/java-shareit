package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundException(final NotFoundException e) {
        log.info("Данные не найдены: {}", e.getMessage());
        return new ErrorResponse("NOT FOUND", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerOwnerException(final OwnerException e) {
        log.info("Ошибка валидации: {}", e.getMessage());
        return new ErrorResponse("NOT FOUND", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerRuntimeException(final RuntimeException e) {
        log.info("Ошибка сервера: {}", e.getMessage());
        return new ErrorResponse("INTERNAL SERVER ERROR", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerBadRequest(final BadRequestException e) {
        log.info("Ошибка во время запроса: {}", e.getMessage());
        return new ErrorResponse("BAD REQUEST", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerStateException(final UnknownStateException e) {
        log.info("Unknown state: {}", e.getMessage());
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
    }
}