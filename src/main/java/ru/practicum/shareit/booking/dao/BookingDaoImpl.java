package ru.practicum.shareit.booking.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.exception.UnknownStateException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
    @Transactional
    public Booking getInfoBooking(int id, int userId) {
        Booking booking = getBookingById(id);
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new OwnerException("Нет доступа для просмотра чужого запроса!");
        }
        return booking;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getAllBookingOneUser(User user, String state) {
        List<Booking> bookingList = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookingList.addAll(bookingRepository.findAllByBookerOrderByStartDesc(user));
                break;
            case "CURRENT":
                bookingList.addAll(bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
                        user, LocalDateTime.now(), LocalDateTime.now()));
                break;
            case "PAST":
                bookingList.addAll(bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(
                        user, LocalDateTime.now()));
                break;
            case "FUTURE":
                bookingList.addAll(bookingRepository.findAllByBookerAndStartAfterOrderByStartDesc(
                        user, LocalDateTime.now()));
                break;
            case "WAITING":
                bookingList.addAll(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                        user, Status.WAITING));
                break;
            case "REJECTED":
                bookingList.addAll(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                        user, Status.REJECTED));
                break;
            default:
                throw new UnknownStateException("Unknown state: " + Status.UNSUPPORTED_STATUS);
        }
        return bookingList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getAllBookingOneOwner(User user, String status) {
        List<Booking> bookingList = new ArrayList<>();
        switch (status) {
            case "ALL":
                bookingList.addAll(bookingRepository.findAllByItemOwnerOrderByStartDesc(user));
                break;
            case "CURRENT":
                bookingList.addAll(bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
                        user, LocalDateTime.now(), LocalDateTime.now()));
                break;
            case "PAST":
                bookingList.addAll(bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(
                        user, LocalDateTime.now()));
                break;
            case "FUTURE":
                bookingList.addAll(bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(
                        user, LocalDateTime.now()));
                break;
            case "WAITING":
                bookingList.addAll(bookingRepository.findAllByItemOwnerAndStatusEqualsOrderByStartDesc(
                        user, Status.WAITING));
                break;
            case "REJECTED":
                bookingList.addAll(bookingRepository.findAllByItemOwnerAndStatusEqualsOrderByStartDesc(
                        user, Status.REJECTED));
                break;
            default:
                throw new UnknownStateException("Unknown state: " + Status.UNSUPPORTED_STATUS);
        }
        return bookingList;
    }

    @Transactional(readOnly = true)
    @Override
    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Запроса с id = " + id + " нет!"));
    }

    @Transactional(readOnly = true)
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
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndIsBefore(userId, itemId, LocalDateTime.now())) {
            throw new BadRequestException("У вас нет доступа чтобы оставить комментарий для вещи, которой вы не пользовались!");
        }
    }
}