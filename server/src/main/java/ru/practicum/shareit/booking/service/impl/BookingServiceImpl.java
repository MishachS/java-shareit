package ru.practicum.shareit.booking.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnknownStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDto addBooking(BookingDtoInput bookingDtoInput, Integer userId) {
        Item item = itemRepository.findById(bookingDtoInput.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещи с таким id не найдена!"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Предмет не доступен для аренды!");
        } else if (item.getOwner().getId() == userId) {
            throw new AccessDeniedException("Вы не можете брать в аренду свои вещи!");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден!"));
        Booking booking = BookingMapper.fromInputBookingDtoToBooking(bookingDtoInput, item, user);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto responseToRequest(int bookingId, int userId, Boolean answer) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Ошибка запроса!"));
        BookingDto dto = BookingMapper.toBookingDto(booking);
        if (dto.getItem().getOwner().getId() != userId) {
            throw new AccessDeniedException("Вы не можете одобрять чужие заявки!");
        } else if (!dto.getStatus().equals(Status.WAITING)) {
            throw new BadRequestException("Предмет уже забронирован!");
        }
        if (answer) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getInfoBooking(int bookingId, int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден!"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Запроса с таким id нет!"));
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new AccessDeniedException("Вы не можете смотреть чужие запросы1");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingOneUser(int userId, String state, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден!"));
        if (from < 0) {
            throw new BadRequestException("Индекс первого элемента не может быть отрицательным!");
        }
        Page<Booking> bookingList;
        Pageable page = PageRequest.of(from / size, size);
        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllByBookerOrderByStartDesc(user, page);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartAsc(
                        user, LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case "PAST":
                bookingList = bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(
                        user, LocalDateTime.now(), page);
                break;
            case "FUTURE":
                bookingList = bookingRepository.findAllByBookerAndStartAfterOrderByStartDesc(
                        user, LocalDateTime.now(), page);
                break;
            case "WAITING":
                bookingList = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                        user, Status.WAITING, page);
                break;
            case "REJECTED":
                bookingList = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                        user, Status.REJECTED, page);
                break;
            default:
                throw new UnknownStateException("Unknown state:" + Status.UNSUPPORTED_STATUS);
        }
        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingOneOwner(int userId, String state, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден!"));
        if (from < 0) {
            throw new BadRequestException("Индекс первого элемента не может быть отрицательным!");
        }
        Page<Booking> bookingList;
        Pageable page = PageRequest.of(from / size, size);
        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllByItemOwnerOrderByStartDesc(user, page);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
                        user, LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case "PAST":
                bookingList = bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(
                        user, LocalDateTime.now(), page);
                break;
            case "FUTURE":
                bookingList = bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(
                        user, LocalDateTime.now(), page);
                break;
            case "WAITING":
                bookingList = bookingRepository.findAllByItemOwnerAndStatusEqualsOrderByStartDesc(
                        user, Status.WAITING, page);
                break;
            case "REJECTED":
                bookingList = bookingRepository.findAllByItemOwnerAndStatusEqualsOrderByStartDesc(
                        user, Status.REJECTED, page);
                break;
            default:
                throw new UnknownStateException("Unknown state:" + Status.UNSUPPORTED_STATUS);
        }
        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

}