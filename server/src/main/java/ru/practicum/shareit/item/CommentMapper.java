package ru.practicum.shareit.item;

import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

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

    public static List<CommentDto> toDto(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toDto).toList();
    }
}

