package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

public class RequestMapper {

    public static ItemRequest toRequest(ItemRequestDto dto, User user) {
        ItemRequest request = new ItemRequest();
        request.setId(dto.getId());
        request.setDescription(dto.getDescription());
        request.setRequester(user);
        return request;
    }

    public static ItemRequestDto toRequestDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setItems(new ArrayList<>());
        return dto;
    }
}
