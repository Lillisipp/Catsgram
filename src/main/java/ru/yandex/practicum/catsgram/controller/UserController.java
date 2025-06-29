package ru.yandex.practicum.catsgram.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import jakarta.validation.Valid;
import ru.yandex.practicum.catsgram.utils.Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
//        log.info("Получен запрос на создание пользователя: {}", user);
        if (users.values().contains(user.getEmail())){
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        user.setId(Utils.getNextIdLong(users)); // присваиваем ID
        users.put(user.getId(), user);
//        log.info("Пользователь создан с ID: {}", user.getId());
        return user;
    }


    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
//            log.warn("Обновление отклонено: ID не указан");
            throw new ConditionsNotMetException("Id должен быть указан.");
        }
        if (!users.containsKey(newUser.getId())) {
//            log.warn("Обновление отклонено: пользователь с ID {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь с таким ID не найден.");
        }

        users.put(newUser.getId(), newUser);
//        log.info("Пользователь с ID {} успешно обновлён", newUser.getId());
        return newUser;
    }
}
