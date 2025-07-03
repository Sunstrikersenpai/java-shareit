package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toEntity(CommentCreateDto dto, User user, Item item) {
        return Comment.builder().author(user).text(dto.getText()).item(item).created(LocalDateTime.now()).build();
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor()
                        .getName()).text(comment.getText())
                .created(comment.getCreated()).build();
    }
}

