package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void serializeUserDto() throws JsonProcessingException {
        UserDto dto = new UserDto(1L, "name", "name@gmail.com");

        String json = mapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"name\"");
        assertThat(json).contains("\"email\":\"name@gmail.com\"");
    }

    @Test
    void deserializeUserDto() throws JsonProcessingException {
        String json = " { \"id\": 2, \"name\": \"name\", \"email\": \"name@gmail.com\"} ";

        UserDto dto = mapper.readValue(json, UserDto.class);

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getName()).isEqualTo("name");
        assertThat(dto.getEmail()).isEqualTo("name@gmail.com");
    }
}