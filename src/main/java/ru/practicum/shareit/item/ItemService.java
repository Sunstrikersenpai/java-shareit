package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long ownerId, ItemShortDto itemShortDto);

    ItemDto update(Long ownerId, Long itemID, ItemShortDto itemShortDto);

    ItemDto get(Long itemId);

    List<ItemDto> findAllByOwnerId(Long ownerId);

    List<ItemDto> search(String text);

    void delete(Long itemId, Long ownerId);

    Item getItemById(Long itemId);

    List<Item> findAllEntityByOwner(Long ownerId);

    CommentDto createComment(CommentCreateDto commentCreateDto, Long userId, Long itemId);

    void updateWithItem(Item item);
}
