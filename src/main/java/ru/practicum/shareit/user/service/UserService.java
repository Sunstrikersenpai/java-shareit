package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    UserDto get(Long userId);

    UserDto create(UserDto user);

    UserDto update(Long id, UserDto userDto);

    void delete(Long userId);

    User getUserById(Long id);
}
