package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto create(Long ownerId, ItemDto itemDto);

    ItemDto update(Long ownerId, Long itemID, ItemDto itemDto);

    ItemDto get(Long itemId);

    List<ItemDto> findAllByOwnerId(Long ownerId);

    List<ItemDto> search(String text);

    void delete(Long itemId,Long ownerId);

    Item getItemById(Long itemId);

    List<Item> findAllEntityByOwner(Long ownerId);
}
