package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItems(ItemDto itemDto, int ownerId);

    ItemDto updateItems(int itemId, ItemDto itemDto, int ownerId);

    ItemDto getItemsById(int itemId, int ownerId);

    List<ItemDto> getAllItemsOneUser(int ownerId, int from, int size);

    List<ItemDto> searchItemByText(String text, int from, int size);

    CommentDto addComment(int itemId, int userId, CommentDto commentDto);
}