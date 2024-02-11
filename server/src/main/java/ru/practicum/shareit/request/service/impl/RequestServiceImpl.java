package ru.practicum.shareit.request.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;
    private List<ItemRequestDto> listDto;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto dto, int requesterId) {
        User user = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден!"));
        ItemRequest request = RequestMapper.toRequest(dto, user);
        request.setCreated(LocalDateTime.now());
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ItemRequestDto> getAllRequestOneUser(int requesterId) {
        User user =  userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден!"));
        List<ItemRequest> requests = requestRepository.findAllByRequesterIdOrderByCreatedAsc(requesterId);
        listDto = new ArrayList<>();

        return getItemRequestDtos(requests, listDto);
    }

    @Override
    public List<ItemRequestDto> getRequestAllUser(int requesterId, int from, int size) {
        User user = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден!"));
        if (from < 0) {
            throw new BadRequestException("Индекс первого элемента не может быть отрицательным!");
        }
        List<ItemRequest> requests = requestRepository
                .findAllByRequesterNotOrderByCreatedAsc(user, PageRequest.of(from, size))
                .stream()
                .collect(Collectors.toList());
        List<ItemRequestDto> listDto = new ArrayList<>();

        return getItemRequestDtos(requests, listDto);
    }

    private List<ItemRequestDto> getItemRequestDtos(List<ItemRequest> requests, List<ItemRequestDto> listDto) {
        List<Integer> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        List<Item> items = itemRepository.findAllByRequestIdIn(requestIds);

        for (ItemRequest request : requests) {
            ItemRequestDto dto = RequestMapper.toRequestDto(request);
            setRequestItems(dto, items);
            listDto.add(dto);
        }

        return listDto;
    }

    @Override
    public ItemRequestDto getRequestById(int requestId, int requesterId) {
        User user =  userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден!"));
        ItemRequestDto dto = RequestMapper.toRequestDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("По вашему id не был найден запрос!")));

        List<Item> items = itemRepository.findAllByRequestId(requestId);
        setRequestItems(dto, items);

        return dto;
    }

    private void setRequestItems(ItemRequestDto itemRequestDto, List<Item> items) {
        itemRequestDto.setItems(items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList())
        );
    }

}