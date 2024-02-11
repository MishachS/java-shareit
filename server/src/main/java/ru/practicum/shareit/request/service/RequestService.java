package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto addRequest(ItemRequestDto dto, int requesterId);

    List<ItemRequestDto> getAllRequestOneUser(int requesterId);

    List<ItemRequestDto> getRequestAllUser(int requesterId, int from, int size);

    ItemRequestDto getRequestById(int requestId, int requesterId);

}