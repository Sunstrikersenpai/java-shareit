package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ItemRequestServiceImpl.class})
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestServiceImpl requestService;

    @Autowired
    private ItemRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setName("Ivan");
        user.setEmail("ivan@example.com");
        user = userRepository.save(user);
    }

    @Test
    void createRequest_shouldSaveAndReturnDto() {
        ItemRequestShortDto shortDto = new ItemRequestShortDto("description");
        ItemRequestDto result = requestService.createRequest(user.getId(), shortDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getDescription()).isEqualTo("description");

        Optional<ItemRequest> fromDb = requestRepository.findById(result.getId());
        assertThat(fromDb).isPresent();
    }

    @Test
    void getOwnRequests_shouldReturnList() {
        ItemRequest req = new ItemRequest();
        req.setDescription("description");
        req.setRequestor(user);
        req.setCreated(java.time.LocalDateTime.now());
        requestRepository.save(req);

        List<ItemRequestDto> requests = requestService.getOwnRequests(user.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getDescription()).isEqualTo("description");
    }
}
