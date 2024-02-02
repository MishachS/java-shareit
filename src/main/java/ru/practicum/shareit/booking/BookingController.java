package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingDtoInput bookingDtoInput, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.addBooking(bookingDtoInput, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto responseToRequest(@PathVariable Integer bookingId, @RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @RequestParam Boolean approved) {
        return bookingService.responseToRequest(bookingId, userId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto getInfoBooking(@PathVariable Integer bookingId, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getInfoBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingOneUser(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingOneUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingOneOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingOneOwner(userId, state);

    }
}