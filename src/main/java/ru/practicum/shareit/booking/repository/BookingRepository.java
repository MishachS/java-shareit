package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBookerOrderByStartDesc(User booker);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime end);

    List<Booking> findAllByBookerAndStartAfterOrderByStartDesc(User booker, LocalDateTime start);

    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User booker, Status status);


    List<Booking> findAllByItemOwnerOrderByStartDesc(User owner);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(User owner, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(User owner, LocalDateTime end);

    List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(User owner, LocalDateTime start);

    List<Booking> findAllByItemOwnerAndStatusEqualsOrderByStartDesc(User owner, Status status);

    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(int itemId, Status status, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(int itemId, Status status, LocalDateTime dateTime);

    Boolean existsByBookerIdAndItemIdAndEndIsBefore(int userId, int itemId, LocalDateTime time);
}