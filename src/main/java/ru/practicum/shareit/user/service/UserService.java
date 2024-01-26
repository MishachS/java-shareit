package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    public final UserDao userDao;

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userDao.addUser(user));
    }

    public UserDto updateUser(UserDto userDto, int id) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userDao.updateUser(user, id));
    }

    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userDao.getUserById(id));
    }

    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
