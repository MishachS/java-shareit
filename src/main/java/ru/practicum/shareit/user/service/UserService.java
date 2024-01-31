package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserDao userDao;

    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userDao.addUser(user));
    }

    @Transactional
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

    @Transactional
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }
}
