package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface BookingDao {
    Booking addBooking(Booking booking);

    Booking responseToRequest(Booking booking, boolean answer);

    Booking getInfoBooking(int id, int userId);

    List<Booking> getAllBookingOneUser(User user, String state, int from, int size);

    List<Booking> getAllBookingOneOwner(User user, String state, int from, int size);

    Booking getBookingById(Integer id);

    Optional<Booking> getLast(int id);

    Optional<Booking> getNext(int id);

    void checkUserBooking(Integer userId, Integer itemId);
}
