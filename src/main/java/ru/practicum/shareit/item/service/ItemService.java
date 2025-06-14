package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long ownerId, ItemDto itemDto);

    ItemDto update(Long ownerId, Long itemID, ItemDto itemDto);

    ItemDto get(Long itemId);

    List<ItemDto> findAllByOwner(Long ownerId);

    List<ItemDto> search(String text);

    void delete(Long itemId,Long ownerId);
}
