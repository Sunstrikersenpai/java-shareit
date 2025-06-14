package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public UserDto get(Long userId) {
        return UserMapper.toDto(getUserById(userId));
    }

    @Override
    public UserDto create(UserDto userDto) {
        if (isEmailExists(userDto.getEmail())) {
            throw new EmailExistsException("Email exists");
        }
        User user = UserMapper.toEntity(userDto);
        return UserMapper.toDto(userStorage.create(user));
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User existing = getUserById(id);
        if (!existing.getEmail().equals(userDto.getEmail())
                && isEmailExists(userDto.getEmail())) {
            throw new EmailExistsException("Email exists");
        }
        patchUser(existing, userDto);
        return UserMapper.toDto(userStorage.update(id, existing));
    }

    @Override
    public void delete(Long userId) {
        itemStorage.deleteItemsByUserId(userId);
        userStorage.delete(userId);
    }

    private boolean isEmailExists(String email) {
        return userStorage.findAll().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    public User getUserById(Long id) {
        return userStorage.get(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void patchUser(User target, UserDto userDto) {
        if (userDto.getName() != null) {
            target.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            target.setEmail(userDto.getEmail());
        }
    }
}
