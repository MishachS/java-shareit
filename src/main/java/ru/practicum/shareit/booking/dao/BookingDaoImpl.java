package ru.practicum.shareit.booking.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnknownStateException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class BookingDaoImpl implements BookingDao {

    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public Booking addBooking(Booking booking) {
        booking.setStatus(Status.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking responseToRequest(Booking booking, boolean answer) {
        if (answer) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getInfoBooking(int id, int userId) {
        Booking booking = getBookingById(id);
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new AccessDeniedException("Нет доступа для просмотра чужого запроса!");
        }
        return booking;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getAllBookingOneUser(User user, String state, int from, int size) {
        Page<Booking> bookingList;
        Pageable page = PageRequest.of(from / size, size);
        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllByBookerOrderByStartDesc(user, page);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
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
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getAllBookingOneOwner(User user, String state, int from, int size) {
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
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Запроса с id = " + id + " нет!"));
    }

    @Override
    public Optional<Booking> getLast(int id) {
        return bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
                id, Status.APPROVED, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Booking> getNext(int id) {
        return bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                id, Status.APPROVED, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    @Override
    public void checkUserBooking(Integer userId, Integer itemId) {
        if (!bookingRepository.existsByBookerIdAndItemIdAndStartIsBefore(userId, itemId, LocalDateTime.now())) {
            throw new BadRequestException("У вас нет доступа чтобы оставить комментарий для вещи, которой вы не пользовались!");
        }
    }
}