package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> get(Long userId);

    User create(User user);

    User update(Long id, User user);

    void delete(Long userId);

    List<User> findAll();
}
