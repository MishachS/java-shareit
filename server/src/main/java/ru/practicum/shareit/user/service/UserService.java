package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, int id);

    UserDto getUserById(int id);

    List<UserDto> getAllUser();

    void deleteUser(int id);
}