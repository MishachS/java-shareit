package ru.practicum.shareit.requestTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByRequesterIdOrderByCreatedAsc() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("tset@email.com");
        userRepository.save(user);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("test");
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.of(2023, 11, 11, 11, 11, 1));
        requestRepository.save(itemRequest);

        List<ItemRequest> requests = requestRepository.findAllByRequesterIdOrderByCreatedAsc(1);
        Assertions.assertEquals(requests.get(0), itemRequest);
    }

    @Test
    void findAllByRequesterNotLikeOrderByCreatedAsc() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("test1");
        user1.setEmail("tset1@email.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setName("test2");
        user2.setEmail("tset2@email.com");
        userRepository.save(user2);

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setId(1);
        itemRequest1.setDescription("test1");
        itemRequest1.setRequester(user2);
        itemRequest1.setCreated(LocalDateTime.of(2023, 11, 11, 11, 11, 1));
        requestRepository.save(itemRequest1);

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(2);
        itemRequest2.setDescription("test2");
        itemRequest2.setRequester(user2);
        itemRequest2.setCreated(LocalDateTime.of(2023, 11, 11, 11, 11, 2));
        requestRepository.save(itemRequest2);


        PageRequest page = PageRequest.of(0, 2);

        List<ItemRequest> requests = requestRepository.findAllByRequesterNotLikeOrderByCreatedAsc(user1, page).toList();

        Assertions.assertEquals(requests.get(0), itemRequest1);
        Assertions.assertEquals(requests.get(1), itemRequest2);
    }
}
