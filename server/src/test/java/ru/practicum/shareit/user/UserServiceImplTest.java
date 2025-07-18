package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class UserServiceImplTest {

    private final UserService userService;

    @Autowired
    UserServiceImplTest(UserRepository userRepository) {
        this.userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUser_success() {
        UserDto userDto = new UserDto();
        userDto.setName("John");
        userDto.setEmail("john@example.com");

        UserDto saved = userService.create(userDto);

        assertNotNull(saved.getId());
        assertEquals("John", saved.getName());
        assertEquals("john@example.com", saved.getEmail());
    }
}
