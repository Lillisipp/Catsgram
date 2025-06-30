package ru.yandex.practicum.catsgram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.utils.Utils;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserService {

    final Map<Long, User> users = new HashMap<>();

    public Collection<User> getUsers() {
        return users.values();
    }

    public User createUser(User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (isEmailAlreadyUsed(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setId(Utils.getNextIdLong(users));
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);// присваиваем ID
        log.info("Пользователь создан с ID: {}", user.getId());
        return user;
    }

    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            log.warn("Обновление отклонено: ID не указан");
            throw new ConditionsNotMetException("Id должен быть указан.");
        }
        User existingUser = users.get(newUser.getId());

        if (!users.containsKey(newUser.getId())) {
            log.warn("Обновление отклонено: пользователь с ID {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь с таким ID не найден.");
        }
        if (newUser.getEmail() != null &&
                !newUser.getEmail().equalsIgnoreCase(existingUser.getEmail()) &&
                isEmailAlreadyUsed(newUser.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        if (newUser.getEmail() != null) {
            existingUser.setEmail(newUser.getEmail());
        }
        if (newUser.getUsername() != null) {
            existingUser.setUsername(newUser.getUsername());
        }
        if (newUser.getPassword() != null) {
            existingUser.setPassword(newUser.getPassword());
        }
        log.info("Пользователь с ID {} успешно обновлён", newUser.getId());
        return existingUser;
    }

    private boolean isEmailAlreadyUsed(String email) {
        return users.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
