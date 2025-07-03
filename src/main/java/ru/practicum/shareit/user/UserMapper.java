package ru.practicum.shareit.user;

public class UserMapper {

    public static User toEntity(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId())
                .build();
    }
}
