package ru.practicum.shareit.user;

public interface UserService {
    UserDto get(Long userId);

    UserDto create(UserDto user);

    UserDto update(Long id, UserDto userDto);

    void delete(Long userId);

    User getUserById(Long id);
}
