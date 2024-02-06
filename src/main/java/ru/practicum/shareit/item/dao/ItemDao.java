package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item addItem(Item item);

    Item updateItem(int itemId, Item item);

    Item getItemById(int itemId);

    List<Item> searchItemByText(String text, int from, int size);

    Comment addComment(Comment comment);

    List<Comment> getAllCommentOneItem(int id);

    List<Item> getAllItemsByOneRequest(int requestId);

    Page<Item> findAllByOwnerId(Integer ownerId, Pageable pageable);

    List<Item> findAllByRequestIdIn(List<Integer> requestIds);
}
