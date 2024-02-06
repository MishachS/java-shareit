package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookingRepository bookingRepository;

    PageRequest page = PageRequest.of(0, 2);

    User user1, user2 = new User();


    Item item1 = new Item();

    @BeforeEach
    void loadInitial() {
        user1 = new User();
        user1.setId(1);
        user1.setName("test1");
        user1.setEmail("tset@email.com1");
        userRepository.save(user1);

        user2 = new User();
        user2.setId(2);
        user2.setName("test2");
        user2.setEmail("test2@test.com");
        userRepository.save(user2);

        item1 = new Item();
        item1.setId(1);
        item1.setName("test1");
        item1.setDescription("test1");
        item1.setAvailable(true);
        item1.setOwner(user1);
        itemRepository.save(item1);
    }

    @Test
    void findAllByBookerOrderByStartDesc() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2024, 10, 11, 11, 11, 1));
        booking.setEnd(LocalDateTime.of(2024, 11, 11, 11, 11, 2));
        booking.setItem(item1);
        booking.setBooker(user1);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        List<Booking> list = bookingRepository.findAllByBookerOrderByStartDesc(user1, page).toList();
        Assertions.assertEquals(list.get(0), booking);
    }

    @Test
    void findAllByBookerAndEndBeforeOrderByStartDesc() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2022, 11, 11, 11, 11, 2));
        booking.setEnd(LocalDateTime.now());
        booking.setItem(item1);
        booking.setBooker(user1);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        List<Booking> list = bookingRepository
                .findAllByBookerAndEndBeforeOrderByStartDesc(
                        user1, LocalDateTime.now(), page).toList();
        Assertions.assertEquals(list.get(0), booking);
    }

    @Test
    void findAllByBookerAndStatusEqualsOrderByStartDesc() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2024, 10, 11, 11, 11, 1));
        booking.setEnd(LocalDateTime.of(2024, 11, 11, 11, 11, 2));
        booking.setItem(item1);
        booking.setBooker(user1);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        List<Booking> list = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                user1, Status.WAITING, page).toList();
        Assertions.assertEquals(list.get(0), booking);
    }

    @Test
    void findAllByItemOwnerOrderByStartDesc() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2024, 10, 11, 11, 11, 1));
        booking.setEnd(LocalDateTime.of(2024, 11, 11, 11, 11, 2));
        booking.setItem(item1);
        booking.setBooker(user1);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        List<Booking> list = bookingRepository.findAllByItemOwnerOrderByStartDesc(user1, page).toList();
        Assertions.assertEquals(list.get(0), booking);
    }

    @Test
    void findAllByItemOwnerAndEndBeforeOrderByStartDesc() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2022, 11, 11, 11, 11, 2));
        booking.setEnd(LocalDateTime.now());
        booking.setItem(item1);
        booking.setBooker(user1);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        List<Booking> list = bookingRepository
                .findAllByItemOwnerAndEndBeforeOrderByStartDesc(
                        user1, LocalDateTime.now(), page).toList();
        Assertions.assertEquals(list.get(0), booking);
    }

    @Test
    void findAllByItemOwnerAndStatusEqualsOrderByStartDesc() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2024, 10, 11, 11, 11, 1));
        booking.setEnd(LocalDateTime.of(2024, 11, 11, 11, 11, 2));
        booking.setItem(item1);
        booking.setBooker(user1);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        List<Booking> list = bookingRepository.findAllByItemOwnerAndStatusEqualsOrderByStartDesc(
                user1, Status.WAITING, page).toList();
        Assertions.assertEquals(list.get(0), booking);
    }


    @Test
    void findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc() {
        Booking booking1 = new Booking();
        booking1.setId(1);
        booking1.setStart(LocalDateTime.now());
        booking1.setEnd(LocalDateTime.of(2024, 11, 11, 11, 11, 2));
        booking1.setItem(item1);
        booking1.setBooker(user1);
        booking1.setStatus(Status.APPROVED);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setId(1);
        booking2.setStart(LocalDateTime.now());
        booking2.setEnd(LocalDateTime.of(2024, 11, 12, 11, 11, 2));
        booking2.setItem(item1);
        booking2.setBooker(user1);
        booking2.setStatus(Status.APPROVED);
        bookingRepository.save(booking2);


        Optional<Booking> last = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
                item1.getId(), Status.APPROVED, LocalDateTime.now());
        Assertions.assertEquals(last.get(), booking2);
    }

    @Test
    void existsByBookerIdAndItemIdAndStartIsBefore() {
        Booking booking1 = new Booking();
        booking1.setId(1);
        booking1.setStart(LocalDateTime.of(2024, 10, 11, 11, 11, 1));
        booking1.setEnd(LocalDateTime.of(2024, 11, 11, 11, 11, 2));
        booking1.setItem(item1);
        booking1.setBooker(user1);
        booking1.setStatus(Status.WAITING);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setStart(LocalDateTime.of(2024, 10, 12, 11, 11, 1));
        booking2.setEnd(LocalDateTime.of(2024, 11, 12, 11, 11, 2));
        booking2.setItem(item1);
        booking2.setBooker(user2);
        booking2.setStatus(Status.WAITING);
        bookingRepository.save(booking2);

        boolean result = bookingRepository.existsByBookerIdAndItemIdAndStartIsBefore(
                user1.getId(), item1.getId(), LocalDateTime.now());
        Assertions.assertFalse(result);
    }
}