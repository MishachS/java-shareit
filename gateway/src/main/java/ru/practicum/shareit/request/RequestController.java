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
        return requestClient.addRequest(requestDto, requesterId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestOneUser(@RequestHeader("X-Sharer-User-Id") Integer requesterId) {
        return requestClient.getAllRequestOneUser(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsAllUsers(
            @RequestHeader("X-Sharer-User-Id") Integer requesterId,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "20", required = false) Integer size) {
        return requestClient.getRequestsAllUsers(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Integer requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Integer requesterId) {
        return requestClient.getRequestById(requestId, requesterId);
    }
}