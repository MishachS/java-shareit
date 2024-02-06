package ru.practicum.shareit.request.dao;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface RequestDao {
    ItemRequest addRequest(ItemRequest request);

    List<ItemRequest> getAllRequestsForUser(int requesterId);

    List<ItemRequest> getRequestsAllUser(User requester, int from, int size);

    ItemRequest getRequestById(int id);
}