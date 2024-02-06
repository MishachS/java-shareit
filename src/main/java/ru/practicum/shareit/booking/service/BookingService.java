package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dao.BookingDao;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {
    private final BookingDao bookingDao;
    private final UserDao userDao;
    private final ItemDao itemDao;

    @Transactional
    public BookingDto addBooking(BookingDtoInput bookingDtoInput, Integer userId) {
        Item item = itemDao.getItemById(bookingDtoInput.getItemId());
        if (!item.getAvailable()) {
            throw new BadRequestException("Предмет не доступен для аренды!");
        } else if (item.getOwner().getId() == userId) {
            throw new AccessDeniedException("Вы не можете брать в аренду свои вещи!");
        }
        User user = userDao.getUserById(userId);
        Booking booking = BookingMapper.fromInputBookingDtoToBooking(bookingDtoInput, item, user);
        return BookingMapper.toBookingDto(bookingDao.addBooking(booking));
    }

    @Transactional
    public BookingDto responseToRequest(int bookingId, int userId, Boolean answer) {
        BookingDto dto = BookingMapper.toBookingDto(bookingDao.getBookingById(bookingId));
        if (dto.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("Вы не можете одобрять чужие заявки!");
        } else if (!dto.getStatus().equals(Status.WAITING)) {
            throw new BadRequestException("Предмет уже забронирован!");
        }
        return BookingMapper.toBookingDto(bookingDao.responseToRequest(BookingMapper.toBooking(dto), answer));
    }

    @Transactional(readOnly = true)
    public BookingDto getInfoBooking(int bookingId, int userId) {
        userDao.getUserById(userId);
        return BookingMapper.toBookingDto(bookingDao.getInfoBooking(bookingId, userId));
    }

    public List<BookingDto> getAllBookingOneUser(int userId, String state, int from, int size) {
        User user = userDao.getUserById(userId);
        if (from < 0) {
            throw new BadRequestException("Индекс первого элемента не может быть отрицательным");
        }
        return bookingDao.getAllBookingOneUser(user, state, from, size)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getAllBookingOneOwner(int userId, String state, int from, int size) {
        User user = userDao.getUserById(userId);
        if (from < 0) {
            throw new BadRequestException("Индекс первого элемента не может быть отрицательным");
        }
        return bookingDao.getAllBookingOneOwner(user, state, from, size)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
