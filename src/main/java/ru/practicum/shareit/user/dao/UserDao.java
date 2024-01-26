package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User addUser(User user);
    User updateUser(User user, int id);
    User getUserById(int id);
    List<User> getAllUsers();
    void deleteUser(int id);
    void checkUserIdInStorage(int id);
}