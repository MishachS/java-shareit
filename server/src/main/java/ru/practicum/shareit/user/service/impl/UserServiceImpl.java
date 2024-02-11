package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, int id) {
        User user = UserMapper.toUser(userDto);
        User originalUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден!"));
        Optional.ofNullable(user.getEmail()).ifPresent(originalUser::setEmail);
        Optional.ofNullable(user.getName()).ifPresent(originalUser::setName);
        return UserMapper.toUserDto(userRepository.save(originalUser));
    }

    @Override
    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден!")));
    }

    @Override
    public List<UserDto> getAllUser() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}