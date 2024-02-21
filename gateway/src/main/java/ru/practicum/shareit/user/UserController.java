package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Метод addUser");
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto, @PathVariable Integer userId) {
        log.info("Метод updateUser userId " + userId);
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer userId) {
        log.info("Метод getUserById userId " + userId);
        return userClient.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUser() {
        log.info("Метод getAllUser");
        return userClient.getAllUser();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer userId) {
        log.info("Метод deleteUser userId " + userId);
        return userClient.deleteUser(userId);
    }
}