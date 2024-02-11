package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestRepository requestRepository;

    @Test
    void findAllByRequestId() {
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

        Item item = new Item();
        item.setId(1);
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(itemRequest);
        itemRepository.save(item);

        List<Item> itemList = itemRepository.findAllByRequestId(1);

        Assertions.assertEquals(itemList.get(0), item);


    }

    @Test
    void findAllByOwnerId() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("tset@email.com");
        userRepository.save(user);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName("test1");
        item1.setDescription("test1");
        item1.setAvailable(true);
        item1.setOwner(user);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("test2");
        item2.setDescription("test2");
        item2.setAvailable(true);
        item2.setOwner(user);
        itemRepository.save(item2);

        PageRequest page = PageRequest.of(0, 2);
        List<Item> items = itemRepository.findAllByOwnerId(1, page).toList();
        Assertions.assertEquals(items.get(0), item1);
        Assertions.assertEquals(items.get(1), item2);
    }
}
