package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingClientDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.UnknownStateException;

import javax.validation.Valid;

@Controller
@RequestMapping("/bookings")
@Slf4j
@Validated
@RequiredArgsConstructor
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> addBooking(@Valid @RequestBody BookingClientDto bookingClientDto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
		if (bookingClientDto.getStart().isAfter(bookingClientDto.getEnd()) ||
				bookingClientDto.getStart().equals(bookingClientDto.getEnd())) {
			throw new BadRequestException("Ошибка в дате аренды!");
		}
		return bookingClient.addBooking(bookingClientDto, userId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> responseToRequest(@PathVariable Integer bookingId, @RequestHeader("X-Sharer-User-Id") Integer userId,
													@RequestParam Boolean approved) {
		return bookingClient.responseToRequest(bookingId, userId, approved);
	}

	@GetMapping("{bookingId}")
	public ResponseEntity<Object> getInfoBooking(@PathVariable Integer bookingId, @RequestHeader("X-Sharer-User-Id") Integer userId) {
		return bookingClient.getInfoBooking(bookingId, userId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllBookingOneUser(@RequestHeader("X-Sharer-User-Id") Integer userId,
													   @RequestParam(defaultValue = "ALL") String state,
													   @RequestParam(defaultValue = "0", required = false) Integer from,
													   @RequestParam(defaultValue = "20", required = false) Integer size) {
		BookingState states = BookingState.from(state)
				.orElseThrow(() -> new UnknownStateException("Неизвестный параметр " + state));
		return bookingClient.getAllBookingOneUser(userId, states, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllBookingOneOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
														@RequestParam(defaultValue = "ALL") String state,
														@RequestParam(defaultValue = "0", required = false) Integer from,
														@RequestParam(defaultValue = "20", required = false) Integer size) {
		BookingState states = BookingState.from(state)
				.orElseThrow(() -> new UnknownStateException("Неизвестный параметр " + state));
		return bookingClient.getAllBookingOneOwner(userId, states, from, size);

	}
}