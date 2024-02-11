package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    void findByItemId() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("tset@email.com");
        userRepository.save(user);

        Item item = new Item();
        item.setId(1);
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);

        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("test");
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.of(2023, 11, 11, 11, 11, 1));
        commentRepository.save(comment);

        List<Comment> list = commentRepository.findByItemId(item.getId());
        Assertions.assertEquals(comment, list.get(0));
    }
}
