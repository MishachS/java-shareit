package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private RequestService requestService;

    @PostMapping
    public ItemRequestDto addRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                     @RequestHeader("X-Sharer-User-Id") Integer requesterId) {
        log.info("Метод addRequest user = " + "X-Sharer-User-Id");
        return requestService.addRequest(itemRequestDto, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllRequestOneUser(@RequestHeader("X-Sharer-User-Id") Integer requesterId) {
        log.info("Метод getAllRequestOneUser user = " + "X-Sharer-User-Id");
        return requestService.getAllRequestOneUser(requesterId);
    }


    @GetMapping("/all")
    public List<ItemRequestDto> getRequestsAllUsers(@RequestHeader("X-Sharer-User-Id") Integer requesterId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                    @Positive @RequestParam(defaultValue = "20", required = false) Integer size) {
        log.info("Метод getRequestsAllUsers user = " + "X-Sharer-User-Id" + " from = " + from + " size = " + size);
        return requestService.getRequestAllUser(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable Integer requestId,
                                         @RequestHeader("X-Sharer-User-Id") Integer requesterId) {
        log.info("Метод getRequestById user = " + "X-Sharer-User-Id" + " Id = " + requesterId);
        return requestService.getRequestById(requestId, requesterId);
    }
}