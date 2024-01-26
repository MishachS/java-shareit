package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ItemDaoImpl implements ItemDao {
    private int nextId = 1;
    public HashMap<Integer, Item> itemStorage = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        item.setId(nextId++);
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(int itemId, Item item) {
        checkItemIdInStorage(itemId);
        Item itemInStorage = itemStorage.get(itemId);
        if (!itemInStorage.getOwner().equals(item.getOwner())) {
            throw new OwnerException("Вы не являетесь владелцем этого объявления");
        }
        if (item.getName() != null) {
            itemInStorage.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemInStorage.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemInStorage.setAvailable(item.getAvailable());
        }
        return itemInStorage;
    }

    @Override
    public Item getItemById(int id) {
        checkItemIdInStorage(id);
        return itemStorage.get(id);
    }

    @Override
    public List<Item> getAllItemsForUser(int ownerId) {
        List<Item> list = new ArrayList<>();
        for (Item item : itemStorage.values()) {
            if (item.getOwner() == ownerId) {
                list.add(item);
            }
        }
        return list;
    }

    @Override
    public List<Item> searchItemByText(String text) {
        List<Item> list = new ArrayList<>();
        for (Item item : itemStorage.values()) {
            if (containsText(item, text) && item.getAvailable()) {
                list.add(item);
            }
        }
        return list;
    }

    private boolean containsText(Item item, String text) {
        return item.getName().toLowerCase().contains(text.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(text.toLowerCase());
    }

    private void checkItemIdInStorage(int id) {
        if (!itemStorage.containsKey(id)) {
            throw new NotFoundException("Вещи по вашему id = " + id + " не найдена!");
        }
    }
}
