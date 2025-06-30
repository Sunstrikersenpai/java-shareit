package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailExistsException;
import ru.practicum.shareit.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User existing = getUserById(id);
        if (!existing.getEmail().equals(userDto.getEmail())
                && isEmailExists(userDto.getEmail())) {
            throw new EmailExistsException("Email exists");
        }
        patchUser(existing,userDto);
        return UserMapper.toDto(userRepository.save(existing));
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(getUserById(userId));
    }

    private boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
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
