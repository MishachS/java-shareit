package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/requests")
@Slf4j
@Validated
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestClient;


    @PostMapping
    public ResponseEntity<Object> addRequest(@Valid @RequestBody RequestDto requestDto,
                                             @RequestHeader("X-Sharer-User-Id") Integer requesterId) {
        log.info("Метод addRequest user = " + "X-Sharer-User-Id");
        return requestClient.addRequest(requestDto, requesterId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestOneUser(@RequestHeader("X-Sharer-User-Id") Integer requesterId) {
        log.info("Метод getAllRequestOneUser user = " + "X-Sharer-User-Id");
        return requestClient.getAllRequestOneUser(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsAllUsers(
            @RequestHeader("X-Sharer-User-Id") Integer requesterId,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "20", required = false) Integer size) {
        log.info("Метод getRequestsAllUsers user = " + "X-Sharer-User-Id" + " from = " + from + " size = " + size);
        return requestClient.getRequestsAllUsers(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Integer requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Integer requesterId) {
        log.info("Метод getRequestById user = " + "X-Sharer-User-Id" + " Id = " + requesterId);
        return requestClient.getRequestById(requestId, requesterId);
    }
}