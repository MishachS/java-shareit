package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {
    private int nextId = 1;
    HashMap<Integer, User> userStorage = new HashMap<>();
    @Override
    public User addUser(User user) {
        checkUserEmail(user);
        user.setId(nextId++);
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user, int id) {
        checkUserIdInStorage(id);
        User userInStorage = userStorage.get(id);
        if (user.getName() != null) {
            userInStorage.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().equals(userInStorage.getEmail())) {
            checkUserEmail(user);
            userInStorage.setEmail(user.getEmail());
        }
        return userInStorage;
    }

    @Override
    public User getUserById(int id) {
        checkUserIdInStorage(id);
        return userStorage.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public void deleteUser(int id) {
        checkUserIdInStorage(id);
        userStorage.remove(id);
    }

    private void checkUserEmail(User user) {
        for (User userInStorage : userStorage.values()) {
            if (userInStorage.getEmail().equals(user.getEmail())) {
                throw new EmailException("Пользователь с таким email уже есть!");
            }
        }
    }

    @Override
    public void checkUserIdInStorage(int id) {
        if (!userStorage.containsKey(id)) {
            throw new NotFoundException("Пользователь с таким id = " + id + " не найден");
        }
    }
}
