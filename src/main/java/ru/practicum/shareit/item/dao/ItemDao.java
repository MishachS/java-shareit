package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item addItem(Item item);

    Item updateItem(int itemId, Item item);

    Item getItemById(int itemId);

    List<Item> getAllItemsForUser(int ownerId);

    List<Item> searchItemByText(String text);
}
